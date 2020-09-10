package com.ajoy.etol.app;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.EnglishTransliterator;
import com.ajoy.etol.mapper.DefaultCharSequenceMapper;

public class E2LUtil 
{
	private static Logger log = LogManager.getLogger(E2LUtil.class);
	private static EnglishTransliterator englishTransliterator ;
	
	
	public static void init()
	{
		DefaultCharSequenceMapper mapper = new DefaultCharSequenceMapper();
		mapper.setTransliterationMarkedup(false);
		try
		{
			englishTransliterator = new EnglishTransliterator(mapper);
		}
		catch(Exception exp)
		{
			log.error("Error",  exp);
		}
	}
	
	public static final String getLanguageString(String asciString) throws IOException
	{
		return englishTransliterator.transliterateString(asciString);
	}
}
