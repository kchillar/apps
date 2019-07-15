package com.ajoy.etol;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;
import com.ajoy.etol.config.Settings;
import com.ajoy.etol.mapper.EnglishCharSequenceToLanguageMapper;


/**
 * @author: Kalyan Chillara<br>
 * 
 * This class reads ascii character by character from the input and buffers them. The buffers are processed <br>
 * and when the sequence matches a phonetic, outputs the language character matching the sequence and <br> 
 * clears the buffer<br>
 * 
 * The phonetic sequences and the language characters to be output are configured in XML configuration file.<br>
 * The only constraint to be followed in XML configuration file is that ascii characters used for vowels should not be used for consonants and vice-versa.<br>
 * 
 * Logic flow is :<br>
 * 0) process each char from the input:<br> 
 * 1) if char is vowel, store in vowel buffer<br>
 * 2) if char is consonant, store in consonant buffer<br>
 * 3) if char is other:<br> 
 *    a) if char is markup, store in markup buffer<br>
 *    b) otherwise write the char to output as there is no processing<br>
 *    
 * The vowel buffer is processed:<br>
 * a) if the buffer reaches the limit<br>
 * b) if char encountered is consonant or other char<br>
 * 
 * The consonant buffer is processed:<br>
 * a) if the buffer reaches the limit<br>
 * b) if char encountered is vowel or other char<br>
 * 
 * Both vowel buffer and consonant buffers are processed if char encountered is other.<br>
 * a) if the last char before other char, is vowel, consonant buffer is processed and then vowel buffer<br>
 * b) if the last char before other char, is consonant, vowel buffer is processed and then consonant buffer<br> 
 * 
 * A flag, shouldTransliterate is set/unset based conditions like, initial config, encountering of chars which <br>
 * represent markup start and markup end pattern<br> 
*
 */
public class EnglishTransliterator
{
	private static Logger log = LogManager.getLogger(EnglishTransliterator.class);
		
	private static final int BufferSize = 10;		

	/** char mapper used to map ascii sequence to language codepoints */
	private EnglishCharSequenceToLanguageMapper mapper;
	private Reader from;	
	private Writer to;

	/** state variables */
	private boolean shouldTransliterate = false;
	private char lastChar = 0;
	private int lastOutputtedKeyType = CharSequenceToCodePointMapping.Other;

	private char[] hallu = new char[BufferSize];
	private int halluIndx = 0;

	private char[] achu = new char[BufferSize];	
	private int achuIndx = 0;

	private String[] keys = new String[BufferSize];
	private int keyIndx = 0;

	private char[] markup = new char[2];
	private int markupIndx = 0;

	/** Log files for information */
	private BufferedWriter phoneticLog;
	private BufferedWriter codePointLog;


	public EnglishTransliterator(Reader input, Writer output, EnglishCharSequenceToLanguageMapper mapper)	
	{		
		this.from = input;
		this.to = output;
		this.mapper = mapper;

		if(mapper.isTransliterationMarkedup())				
			shouldTransliterate = false;		
		else			
			shouldTransliterate = true;		
	}

	private void createLogFiles() throws IOException
	{
		if(Settings.GeneratePhoneticData)
			phoneticLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("phonetics.txt"))));

		if(Settings.GenerateCodePointsData)
			codePointLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("codepoints.txt")))); 
	}

	private void closeLogFiles() throws IOException
	{
		if(Settings.GeneratePhoneticData)
		{
			phoneticLog.flush();
			phoneticLog.close();
		}		
		if(Settings.GenerateCodePointsData)
		{
			codePointLog.flush();
			codePointLog.close();
		}
	}

	
	public void start() throws IOException 
	{								
		createLogFiles();
		long startTime = System.currentTimeMillis();
		try
		{
			startTime = System.currentTimeMillis();
			do
			{	
				int val = from.read();					
				if(val == -1)
					break;												
				processAsciChar((char)val);
			}
			while(true);
			long endTime = System.currentTimeMillis();
			System.out.println("completed in "+ (endTime-startTime) +" millis seconds.");
		}
		finally
		{
			try
			{
				if(to != null)				
					to.flush();

				closeLogFiles();
			}
			catch(Exception io)
			{
				io.printStackTrace();
			}
		}
	}

	/**
	 *    
	 * @param currChar
	 * @throws IOException
	 */
	private void processAsciChar(char currChar) throws IOException
	{					
		if(isAsciiCharPartOfAchulu(currChar) && (shouldTransliterate))
		{
			markupIndx = 0;
			if(isAsciiCharUsedInMarkup(lastChar))
				processMarkupLike(lastChar);

			if(log.isDebugEnabled())
				log.debug("A-processAsciChar('"+currChar+"')");			
			if(halluIndx>0)
				processHalluBuffer();
			achu[achuIndx++] = currChar;			
			if(achuIndx == achu.length-1)
				processAchuBuffer();			
			lastChar = currChar;
		}
		else if(isAsciiCharPartOfHallulu(currChar) && (shouldTransliterate))
		{	
			markupIndx = 0;
			if(isAsciiCharUsedInMarkup(lastChar))
				processMarkupLike(lastChar);

			if(log.isDebugEnabled())
				log.debug("H-processAsciChar('"+currChar+"')");
			if(achuIndx>0)
				processAchuBuffer();
			hallu[halluIndx++] = currChar;			
			if(halluIndx == hallu.length-1)
				processHalluBuffer();			
			lastChar = currChar;
		}
		else
		{									
			if(isAsciiCharPartOfAchulu(lastChar))
			{
				if(halluIndx>0)
					processHalluBuffer();
				if(achuIndx>0)
					processAchuBuffer();
			}
			else if(isAsciiCharPartOfHallulu(lastChar))
			{
				if(achuIndx>0)
					processAchuBuffer();
				if(halluIndx>0)
					processHalluBuffer();
			}

			if(isAsciiCharUsedInMarkup(currChar))
			{
				if(log.isDebugEnabled())
					log.debug("M-processAsciChar('"+currChar+"')");

				markup[markupIndx++] = currChar;

				if(markupIndx == 2)
				{
					String key = new String(markup, 0, 2);

					if(mapper.getMarkupStart().equalsIgnoreCase(key))
					{				
						if(mapper.isTransliterationMarkedup())
							shouldTransliterate = true;
						else
							shouldTransliterate = false;

						markupIndx = 0;
						lastChar = 0;
					}
					else if(mapper.getMarkupEnd().equalsIgnoreCase(key))
					{
						if(mapper.isTransliterationMarkedup())
							shouldTransliterate = false;
						else
							shouldTransliterate = true;	
						markupIndx = 0;
						lastChar = 0;
					}
					else
					{
						processMarkupLike(markup[0]);						
						markup[0] = currChar;
						markupIndx = 1;
						lastChar = currChar;
					}
				}
				else				
					lastChar = currChar;
			}
			else
			{
				markupIndx = 0;
				if(isAsciiCharUsedInMarkup(lastChar))
					processMarkupLike(lastChar);


				if(log.isDebugEnabled())
					log.debug("O-processAsciChar('"+currChar+"')");
				processOther(currChar);
				lastChar = 0;
			}
		}
	}

	private final void processMarkupLike(char aChar) throws IOException
	{
		to.write(aChar);
		if(Settings.GeneratePhoneticData)
			phoneticLog.write(aChar);
		if(Settings.GenerateCodePointsData)
			codePointLog.write(aChar);
	}


	private final void processOther(char currChar) throws IOException
	{
		if(Settings.GeneratePhoneticData)
			phoneticLog.write(currChar);
		if(Settings.GenerateCodePointsData)
			codePointLog.write(currChar);

		if(lastOutputtedKeyType == CharSequenceToCodePointMapping.Consonant)
			to.write(mapper.getVisarga());

		to.write(currChar); 
		lastOutputtedKeyType = CharSequenceToCodePointMapping.Other;
	}


	private void processAchuBuffer() throws IOException
	{
		log.debug("processAchuBuffer("+achuIndx+")");
		keyIndx = 0;
		for(int i=0; i<achuIndx; i++)
		{
			CharSequenceToCodePointMapping ls = null;			

			if(i <= achuIndx - 2)
			{
				ls = mapper.getVowelSymbol(new String(achu, i, 2));			  	
				if(ls != null)								
					i = i+1; 				
				else				
					ls = mapper.getVowelSymbol(achu[i]+"");				
			}
			else			
				ls = mapper.getVowelSymbol(achu[i]+"");

			if(ls != null)			
				keys[keyIndx++] = ls.getAsciiCharSequence();											
		}				
		outputAchus();
		achuIndx = 0;
	}

	private void processHalluBuffer() throws IOException
	{
		log.debug("processHalluBuffer("+halluIndx+")");
		keyIndx = 0;
		for(int i=0; i<halluIndx; i++)
		{
			CharSequenceToCodePointMapping ls = null;			

			if(i <= halluIndx - 2)
			{
				ls = mapper.getConsonantSymbol(new String(hallu, i, 2));			  	
				if(ls != null)								
					i = i+1;				
				else				
					ls = mapper.getConsonantSymbol(hallu[i]+"");				
			}
			else			
				ls = mapper.getConsonantSymbol(hallu[i]+"");

			if(ls != null)			
				keys[keyIndx++] = ls.getAsciiCharSequence();							
		}				
		outputHallus();
		halluIndx = 0;		
	}



	private void outputHallus() throws IOException
	{
		if(log.isDebugEnabled())
			showKeys("outputHallus("+keyIndx+")");

		for(int i=0;i<keyIndx; i++)
		{
			if(Settings.GeneratePhoneticData)
				phoneticLog.write(keys[i]+"|");

			CharSequenceToCodePointMapping ls = null;

			if(i>0)				
				to.write(mapper.getVisarga());				

			ls = mapper.getConsonantSymbol(keys[i]);

			if(ls != null)
			{
				for(int cp: ls.getCodepoints())
					to.write(cp);

				if(Settings.GenerateCodePointsData)				
					codePointLog.write(ls.getHexCodepoint()+"|");				
			}			
			lastOutputtedKeyType = CharSequenceToCodePointMapping.Consonant;
		}		

		to.flush();
	}

	private void outputAchus() throws IOException
	{
		if(log.isDebugEnabled())
			showKeys("outputAchus("+keyIndx+")");

		for(int i=0;i<keyIndx; i++)
		{
			if(Settings.GeneratePhoneticData)
				phoneticLog.write(keys[i]+"|");

			CharSequenceToCodePointMapping ls = null;

			if(lastOutputtedKeyType == CharSequenceToCodePointMapping.Consonant)
			{										
				ls = mapper.getConsonantSymbol(keys[i]);
				if(ls.getHexCodepoint().equals("0C01"))//ignore 'a' after consonant as the consonant symbol is default
					ls = null;
			}
			else if ((lastOutputtedKeyType == CharSequenceToCodePointMapping.Other) || (lastOutputtedKeyType == CharSequenceToCodePointMapping.Vowel))
			{
				ls = mapper.getVowelSymbol(keys[i]);
			}

			if(ls != null)
			{
				for(int cp: ls.getCodepoints())
					to.write(cp);

				if(Settings.GenerateCodePointsData)
					codePointLog.write(ls.getHexCodepoint()+"|");				
			}
			lastOutputtedKeyType = CharSequenceToCodePointMapping.Vowel;
		}	

		to.flush();
	}

	private void showKeys(String msg)
	{
		StringBuilder buff = new StringBuilder();
		for(int i=0;i<keyIndx; i++)
			buff.append(keys[i]+"|");							
		log.debug(msg+" => "+buff.toString());
	}

	private boolean isAsciiCharPartOfHallulu(int c)
	{
		return mapper.isAsciiCharUsedForLanguageConsonant(c);
	}

	private boolean isAsciiCharPartOfAchulu(int c)
	{
		return mapper.isAsciiCharUsedForLanguageVowel(c);
	}

	private boolean isAsciiCharUsedInMarkup(int c)
	{
		return mapper.isAsciiCharUsedInMarkup(c);
	}
}
