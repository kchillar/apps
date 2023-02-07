package com.ajoy.etol;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;
import com.ajoy.etol.config.Settings;
import com.ajoy.etol.mapper.TransliterationMapper;
import com.ajoy.etol.mapper.CharSequenceMapperProvider;


/**
 * @author: Kalyan Chillara<br>
 * 
 * This class reads ascii character by character from the input and buffers them.<br>
 * When the character sequence in the buffer matches a configured sequence, corresponding language character is written to the output stream and the 
 * buffer is cleared.<br> 
 * 
 * The phonetic sequences and the language characters to be output are configured in XML configuration file.<br>
 * The main constraint to be followed in XML configuration file is that same ascii character  cannot be mapped to both vowel and consonant 
 * in the target language.<br>
 * 
 * Logic flow is:<br>
 * 0) While input is available:
 * 0) read a char from the input. If the asci char encountered:<br> 
 * 1) is language vowel, store the asci char in a vowel buffer<br>
 * 2) is language consonant, store the asci char in a consonant buffer<br>
 * 3) is neither of the above:<br> 
 *    a) if char is markup, store in markup buffer<br>
 *    b) otherwise write the char to output as is<br>
 *    
 * The vowel buffer is processed if:<br>
 * a) the buffer reaches the limit<br>
 * b) the asci char encountered is used for language consonant or other char<br>
 * 
 * The consonant buffer is processed if:<br>
 * a) the buffer reaches the limit<br>
 * b) the asci char encountered is vowel or other char<br>
 * 
 * Both the vowel buffer and the consonant buffers are processed if char encountered is other.<br>
 * a) if the last char before other char, is vowel, consonant buffer is processed and then the vowel buffer<br>
 * b) if the last char before other char, is consonant, vowel buffer is processed and then the consonant buffer<br> 
 * 
 * 
 * Processing of a vowel or consonant buffer involves:
 * a) lookup of a language unicode (using mapper) associated with the complete sequence of the asci chars in the buffer<br>
 * b) writing the language unicode to the language output buffer<br>
 * c) and resetting the buffer<br>
 *  
 * A flag, shouldTransliterate is set/unset based conditions encountered like, initial configuration, encountering of chars which <br>
 * represent start of markup and end of markup.<br> 
 * 
 * Finally instances of this class are not thread safe and external sychronization is needed if instance of this class is shared between multiple threads.<br>
 *
 */
public class Transliterator
{
	private static Logger log = LogManager.getLogger(Transliterator.class);
	public static final String LANGUAGE_TELUGU = "te";
	public static final String LANGUAGE_HINDI = "hi";
	
	private static final char NullChar = '\0';	
	private static final int BufferSize = 10;		
	

	/** char mapper used to map ascii sequence to language codepoints */
	private TransliterationMapper mapper;
	//private Reader from;	
	//private Writer to;
	private StringBuilder outBuffer;

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

	public static Transliterator getInstance(String languageCode)
	{
		return new Transliterator(languageCode);
	}
	
	private Transliterator(String languageCode)	
	{		
		this.mapper = CharSequenceMapperProvider.getCharSequenceMapper(languageCode);

		if(mapper.isTransliterationMarkedup())				
			shouldTransliterate = false;		
		else			
			shouldTransliterate = true;		
		
		try
		{
			createLogFiles();
		}
		catch(Exception exp)
		{
			log.error("Exp need to fix this",exp); 
		}
	}

	
	private void createLogFiles() throws IOException
	{
		if(Settings.GeneratePhoneticData)
			phoneticLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("phonetics.txt"))));
		if(Settings.GenerateCodePointsData)
			codePointLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("codepoints.txt")))); 
	}
		
	public String toLanguageString(String phoneticString) throws IOException 
	{				
		if(phoneticString == null)
			return phoneticString;			
		outBuffer = new StringBuilder();			
		for(char c: phoneticString.toCharArray())					
			processAsciChar(c);						
		processAsciChar(NullChar);
		String languageStr = outBuffer.toString();		
		
		//if(Settings.EnableLogs)
		System.out.println("phoneticStr:|"+phoneticString+"|, len:"+phoneticString.length()+", langSring:|"+languageStr+"|, len:"+languageStr.length());
		
		return languageStr;
	}

	
	/**
	 * A language string has following char sequences:<br>
	 * a) Hallu and Gunintam <br>
	 * b) Acchu followed by Hallu
	 * c) Hallu followed by modifier to indicate the next Hallu is a Vottu<br>
	 * 
	 * If ((current is Hallu OR Acchu OR other character) AND (previous is Hallu)) need to output addtional asci 'a' before current asci<br>
	 * 
	 * @param languageString
	 * @return
	 * @throws IOException
	 */
	public String toPhoneticString(String languageString) throws IOException 
	{				
		if(languageString == null)
			return languageString;		
		char[] c = languageString.toCharArray();
		CharSequenceToCodePointMapping prev = null;
		CharSequenceToCodePointMapping curr = null;				
		outBuffer = new StringBuilder();			
		
		for(int i=0; i< c.length; i++)
		{			
			 curr = mapper.getAsciSequence((int)c[i]);
			 processPrev(curr, prev, outBuffer);			 
			 if(curr != null)
			 {
				 System.out.println("Language codepoint c["+i+"] x"+curr.getHexCodepoint()+" type: "+curr.getType()+" mapped asci: '"+curr.getAsciiCharSequence()+"'");													 
				 if(curr.getAsciiCharSequence() != null)
					 outBuffer.append(curr.getAsciiCharSequence());								 
			 }
			 else
			 {
				 outBuffer.append(c[i]);
			 }
			 prev = curr;
			 curr = null;			 
		}	
		
		processPrev(curr, prev, outBuffer);
		String phoneticString = outBuffer.toString();	
		
		//if(Settings.EnableLogs)
		System.out.println("langStr:|"+languageString+"|, len:"+languageString.length()+", phoneticStr:|"+phoneticString+"|, len:"+phoneticString.length());

		return phoneticString;
	}

	private void processPrev(CharSequenceToCodePointMapping curr, CharSequenceToCodePointMapping prev, StringBuilder buff)
	{
		//If ((prev is Hallu) AND (Current is another Hallu OR Acchu OR other character)) need to output asci 'a' before current
		if( (curr == null || curr.getType() == CharSequenceToCodePointMapping.Hallu || curr.getType() == CharSequenceToCodePointMapping.Acchu)				
				&& (prev !=null && prev.getType() == CharSequenceToCodePointMapping.Hallu))
		{
			//System.out.println("adding 'a'");
			buff.append('a');
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

			if(Settings.EnableLogs)
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

			if(Settings.EnableLogs)
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
				if(Settings.EnableLogs)
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


				if(Settings.EnableLogs)
					log.debug("O-processAsciChar('"+currChar+"')");
				processOther(currChar);
				lastChar = 0;
			}
		}
	}

	private final void outputChar(char aChar) throws IOException
	{
		outBuffer.append(aChar);
		
		if(Settings.GeneratePhoneticData)
		{
			phoneticLog.write(aChar);	
			phoneticLog.flush();	
		}
		
		if(Settings.GenerateCodePointsData)
		{
			codePointLog.write(aChar);
			codePointLog.flush();
		}
	}
	
	private final void processMarkupLike(char aChar) throws IOException
	{
		outputChar(aChar);
	}


	private final void processOther(char currChar) throws IOException
	{
		if(lastOutputtedKeyType == CharSequenceToCodePointMapping.Hallu)
		{
			outputChar((char)mapper.getModifierCodepoint());			
		}
		
		if(currChar != NullChar)
			outputChar(currChar);
		lastOutputtedKeyType = CharSequenceToCodePointMapping.Other;
	}


	private void processAchuBuffer() throws IOException
	{
		if(Settings.EnableLogs)
			log.debug("processAchuBuffer("+achuIndx+")");
		
		keyIndx = 0;
		for(int i=0; i<achuIndx; i++)
		{
			CharSequenceToCodePointMapping ls = null;			

			if(i <= achuIndx - 2)
			{
				ls = mapper.getLanguageAcchuUnicode(new String(achu, i, 2));			  	
				if(ls != null)								
					i = i+1; 				
				else				
					ls = mapper.getLanguageAcchuUnicode(achu[i]+"");				
			}
			else			
				ls = mapper.getLanguageAcchuUnicode(achu[i]+"");

			if(ls != null)			
				keys[keyIndx++] = ls.getAsciiCharSequence();											
		}				
		outputAchus();
		achuIndx = 0;
	}

	private void processHalluBuffer() throws IOException
	{
		if(Settings.EnableLogs)
			log.debug("processHalluBuffer("+halluIndx+")");
		
		keyIndx = 0;
		for(int i=0; i<halluIndx; i++)
		{
			CharSequenceToCodePointMapping ls = null;			

			if(i <= halluIndx - 2)
			{
				ls = mapper.getLanguageHalluUnicode(new String(hallu, i, 2));			  	
				if(ls != null)								
					i = i+1;				
				else				
					ls = mapper.getLanguageHalluUnicode(hallu[i]+"");				
			}
			else			
				ls = mapper.getLanguageHalluUnicode(hallu[i]+"");

			if(ls != null)			
				keys[keyIndx++] = ls.getAsciiCharSequence();							
		}				
		outputHallus();
		halluIndx = 0;		
	}


	private void outputHallus() throws IOException
	{
		if(Settings.EnableLogs)
			showKeys("outputHallus("+keyIndx+")");

		for(int i=0;i<keyIndx; i++)
		{
			if(Settings.GeneratePhoneticData)
				phoneticLog.write(keys[i]+"|");

			CharSequenceToCodePointMapping ls = null;

			if(i>0)				
				outputChar((char)mapper.getModifierCodepoint());

			ls = mapper.getLanguageHalluUnicode(keys[i]);

			if(ls != null)
			{
				outputChar((char)ls.getCodepoint());
				if(Settings.GenerateCodePointsData)				
					codePointLog.write(ls.getHexCodepoint()+"|");				
			}			
			lastOutputtedKeyType = CharSequenceToCodePointMapping.Hallu;
		}				
	}

	private void outputAchus() throws IOException
	{
		if(Settings.EnableLogs)
			showKeys("outputAchus("+keyIndx+")");

		for(int i=0;i<keyIndx; i++)
		{
			if(Settings.GeneratePhoneticData)
				phoneticLog.write(keys[i]+"|");

			CharSequenceToCodePointMapping ls = null;

			if(lastOutputtedKeyType == CharSequenceToCodePointMapping.Hallu)
			{										
				ls = mapper.getLanguageHalluUnicode(keys[i]);
				if(ls.getHexCodepoint().equals("0C01"))//ignore 'a' after consonant as the consonant symbol is default
					ls = null;
			}
			else if ((lastOutputtedKeyType == CharSequenceToCodePointMapping.Other) || (lastOutputtedKeyType == CharSequenceToCodePointMapping.Acchu))
			{
				ls = mapper.getLanguageAcchuUnicode(keys[i]);
			}

			if(ls != null)
			{
				outputChar((char)ls.getCodepoint());
				if(Settings.GenerateCodePointsData)
					codePointLog.write(ls.getHexCodepoint()+"|");				
			}
			lastOutputtedKeyType = CharSequenceToCodePointMapping.Acchu;
		}	
	}

	private void showKeys(String msg)
	{
		StringBuilder buff = new StringBuilder();
		for(int i=0;i<keyIndx; i++)
			buff.append(keys[i]+"|");		
		
		if(Settings.EnableLogs)
			log.debug(msg+" => "+buff.toString());
	}

	public void closeLogFiles() throws IOException
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
