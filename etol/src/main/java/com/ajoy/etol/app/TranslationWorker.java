package com.ajoy.etol.app;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.EnglishTransliterator;
import com.ajoy.etol.mapper.DefaultCharSequenceMapper;
import com.ajoy.etol.mapper.EnglishCharSequenceToLanguageMapper;


public class TranslationWorker implements Runnable
{
	private static Logger log = LogManager.getLogger(TranslationWorker.class);

	
	/** The thread to run the transliterator */

	private EnglishTransliterator transliterator;

	public TranslationWorker(Reader reader, Writer wr) throws IOException
	{
		EnglishCharSequenceToLanguageMapper mapper = new DefaultCharSequenceMapper();
		transliterator = new EnglishTransliterator(reader, wr, mapper);
	}

	public void run()
	{
		log.info("run() start");
		try
		{
			transliterator.start();
		}
		catch(Exception exp)
		{
			log.error("Error in writing to output ", exp);
			exp.printStackTrace();
		}
		log.info("run() end");
	}

}
