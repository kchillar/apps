package com.ajoy.etol.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;
import com.ajoy.etol.config.Settings;
import com.ajoy.etol.config.TransliteratorConfig;

/**
 * 
 * @author Kalyana Chillara<br>
 *
 */
public class TransliterationMapperImpl implements TransliterationMapper
{		
	private static Logger log = LogManager.getLogger(TransliterationMapperImpl.class);

	private TransliteratorConfig config; 

	/** All these are populated from the config */
	private CharSequenceToCodePointMapping[] symbolList ;	
	private int modifier;
	private CharSequenceToCodePointMapping modifierSymbol;
	
	private Map<String, CharSequenceToCodePointMapping> consonantMap = new HashMap<String, CharSequenceToCodePointMapping>();
	private Map<String, CharSequenceToCodePointMapping> vowelMap = new HashMap<String, CharSequenceToCodePointMapping>();
	private Map<String, CharSequenceToCodePointMapping> commentMap = new HashMap<String, CharSequenceToCodePointMapping>();
	private Map<String, CharSequenceToCodePointMapping> codepointMap = new HashMap<String, CharSequenceToCodePointMapping>();
	private boolean[] listOfAsciiCharUsedForComments = new boolean[128];
	private boolean[] listOfAsciiCharUsedForLanguageVowel = new boolean[128];
	private boolean[] listOfAsciiCharUsedForLanguageConsonant = new boolean[128];

	public TransliterationMapperImpl(TransliteratorConfig aConfig)
	{				
		
		config = aConfig;
		
		for(int i=0 ; listOfAsciiCharUsedForLanguageConsonant.length < 128; i++)
			listOfAsciiCharUsedForLanguageConsonant[i] = false;

		for(int i=0 ; listOfAsciiCharUsedForLanguageVowel.length < 128; i++)					
			listOfAsciiCharUsedForLanguageVowel[i] = false;

		init();		
		populateFlags();		
	}

	public CharSequenceToCodePointMapping getAsciSequence(int codepoint)
	{
		return codepointMap.get(codepoint+"");
	}

	public void setTransliterationMarkedup(boolean transliterationMarkedup) 
	{
		config.setTransliterationMarkedup(transliterationMarkedup);
	}


	public void init()
	{
		try
		{
			modifier = Integer.parseInt(config.getModifier(), 16);	
			modifierSymbol = new CharSequenceToCodePointMapping(); 
			modifierSymbol.setCodepoint(modifier);
			modifierSymbol.setHexCodepoint(config.getModifier());

			codepointMap.put(modifier+"", modifierSymbol);

			symbolList = new CharSequenceToCodePointMapping[config.getSymbolsList().size()];						
			int i = 0;			
			for(CharSequenceToCodePointMapping sy: config.getSymbolsList())
			{	
				sy.setCodepoint( Integer.parseInt(sy.getHexCodepoint(),16) );

				//prefer to take first mapping defined for reverse conversions
				if(codepointMap.get(sy.getCodepoint()+"") == null)
					codepointMap.put(sy.getCodepoint()+"", sy);

				if(sy.getAsciiCharSequence() == null)
				{
					if(Settings.EnableLogs)
						log.warn("This is most wierd thing. Hard coding 'kh' in code");
					sy.setAsciiCharSequence("kh");
				}

				if(Settings.EnableLogs)
					log.info(" Type: "+sy.getType()+" DEC: "+sy.getCodepoint()+" HEX: "+sy.getHexCodepoint()+" Seq: "+sy.getAsciiCharSequence());

				symbolList[i] = sy;												
				if(sy.getType() == CharSequenceToCodePointMapping.Hallu)
				{					
					consonantMap.put(sy.getAsciiCharSequence(), sy);
				}
				else if(sy.getType() == CharSequenceToCodePointMapping.Acchu)
				{
					vowelMap.put(sy.getAsciiCharSequence(), sy);
				}
				else if(sy.getType() == CharSequenceToCodePointMapping.Gunintam)
				{
					consonantMap.put(sy.getAsciiCharSequence(), sy);
				}
				else if(sy.getType() == CharSequenceToCodePointMapping.CommentChar)
				{
					commentMap.put(sy.getAsciiCharSequence(), sy);
				}				
				else
				{
					throw new IllegalStateException("Invalid type: "+sy);
				}		

				i++;
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}

	private void populateFlags()
	{		
		for(int i=0; i<config.getConsonantCharList().size(); i++)
		{							
			int index = (int) config.getConsonantCharList().get(i).charAt(0);
			listOfAsciiCharUsedForLanguageConsonant[index] = true;
		}			
		for(int i=0; i<config.getVowelCharList().size(); i++)
		{							
			int index = (int) config.getVowelCharList().get(i).charAt(0);
			listOfAsciiCharUsedForLanguageVowel[index] = true;
		}	

		if(config.getMarkupStart() != null)
		{
			for(char c: config.getMarkupStart().toCharArray())
				listOfAsciiCharUsedForComments[c] = true;
		}

		if(config.getMarkupEnd() != null)
		{
			for(char c: config.getMarkupEnd().toCharArray())
				listOfAsciiCharUsedForComments[c] = true;
		}

	}

	public String getMarkupStart()
	{
		return config.getMarkupStart();
	}

	public String getMarkupEnd()
	{
		return config.getMarkupEnd();
	}


	public CharSequenceToCodePointMapping getVowelSymbol(String asciiCharSequence) 
	{
		if(Settings.EnableLogs)
			log.debug("vowl-key: "+asciiCharSequence);

		CharSequenceToCodePointMapping sy = vowelMap.get(asciiCharSequence);		
		if(sy == null)
		{
			if(Settings.EnableLogs)
				log.debug("Not able to find vowel symbol for key: "+asciiCharSequence);
		}

		return sy;
	}

	public CharSequenceToCodePointMapping getConsonantSymbol(String asciiCharSequence) 
	{		
		if(Settings.EnableLogs)
			log.debug("cons-key: "+asciiCharSequence);

		CharSequenceToCodePointMapping sy = consonantMap.get(asciiCharSequence);		
		if(sy == null)
		{
			if(Settings.EnableLogs)
				log.debug("Not able to find consonant symbol for key: "+asciiCharSequence);
		}

		return sy;
	}


	public CharSequenceToCodePointMapping getMarkupSymbol(String asciiCharSequence) 
	{		
		if(Settings.EnableLogs)
			log.debug("comm-key: "+asciiCharSequence);

		CharSequenceToCodePointMapping sy = commentMap.get(asciiCharSequence);

		if(sy == null)
		{
			if(Settings.EnableLogs)
				log.debug("Not able to find comment symbol for key: "+asciiCharSequence);
		}

		return sy;
	}


	public int getModifierCodepoint()
	{
		return modifier;
	}

	public CharSequenceToCodePointMapping getModifierSymbol()
	{
		return modifierSymbol;
	}


	public CharSequenceToCodePointMapping[] getSymbolList() 
	{
		return symbolList;
	}

	public boolean isTransliterationMarkedup()	
	{
		return config.isTransliterationMarkedup();
	}

	public boolean isAsciiCharUsedForLanguageVowel(int c)
	{
		if(c > listOfAsciiCharUsedForLanguageVowel.length - 1)
			return false;

		return  listOfAsciiCharUsedForLanguageVowel[c];
	}

	public boolean isAsciiCharUsedForLanguageConsonant(int c)
	{
		if(c > listOfAsciiCharUsedForLanguageConsonant.length - 1)
			return false;

		return  listOfAsciiCharUsedForLanguageConsonant[c];
	}

	public boolean isAsciiCharPassThrough(int c)
	{
		return !( isAsciiCharUsedForLanguageConsonant(c) || isAsciiCharUsedForLanguageVowel(c));
	}

	public boolean isAsciiCharUsedInMarkup(int c)
	{
		if(c > listOfAsciiCharUsedForComments.length -1)
			return false;

		return listOfAsciiCharUsedForComments[c];
	}
}
