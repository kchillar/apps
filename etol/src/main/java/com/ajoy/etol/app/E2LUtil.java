package com.ajoy.etol.app;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.Transliterator;

public class E2LUtil 
{
	private static Logger log = LogManager.getLogger(E2LUtil.class);
	private static Transliterator transliterator ;
	
	
	public static void init()
	{
		try
		{
			transliterator = Transliterator.getInstance(Transliterator.LANGUAGE_TELUGU);
		}
		catch(Exception exp)
		{
			log.error("Error",  exp);
		}
	}
	
	public static final String getLanguageString(String asciString) throws IOException
	{
		return transliterator.toLanguageString(asciString);
	}
}
