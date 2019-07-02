package com.yoo.etol.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.yoo.etol.EnglishTransliterator;
import com.yoo.etol.LanguageTransliterator;
import com.yoo.etol.mapper.DefaultCharSequenceMapper;

public class App 
{
	private static Logger log = LogManager.getLogger(App.class);
	
	public static void main(String[] args) throws Exception
	{
		if(args.length == 1)
		{
			log.info("Transliterationg from System.in");
			processEtoLFiles(args[0]);
		}
		else if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("E2L"))
			{
				log.info("Transliterationg from English");
				processEtoLFiles(args[0], args[1]);
			}
			else if(args[2].equalsIgnoreCase("L2E"))
			{
				log.info("Transliterationg from Language");
				processLtoEFiles(args[0], args[1]);
			}
		}		
		else
		{
			log.error("Invalid arguments "+args);
		}
	}
	
	
	public static void processEtoLFiles(String outputFile) throws IOException
	{						
		PipedInputStream textIn  =  new PipedInputStream();
		PipedOutputStream textOut  = new PipedOutputStream(textIn);
		Reader inputReader = new InputStreamReader(textIn);									
		Writer fileWriter = new OutputStreamWriter(new FileOutputStream(new File(outputFile)));
		
		Thread translationThread = new Thread(new TranslationWorker(inputReader, fileWriter));
		translationThread.start();

		log.info("Will read from standinput and output to writer");
		 BufferedReader br  = new BufferedReader(new InputStreamReader(System.in)); 

		 String line;
		 
		 while(true)
		 {
			 System.out.print("");
			 line = br.readLine();
			 			 			 
			 textOut.write(line.getBytes());			 
			 textOut.write('\n');
			 
		 }
		 
		 
	}

	public static void processEtoLFiles(String inputFile, String outputFile) throws IOException
	{
		System.out.println("Starting E2L Processing from input: "+inputFile+" and will output to : "+outputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))));
		
		DefaultCharSequenceMapper mapper = new DefaultCharSequenceMapper();
		
		EnglishTransliterator lst = new EnglishTransliterator(br, wr, mapper );						
		lst.start();				
		wr.close();
		br.close();
	}

	public static void processLtoEFiles(String inputFile, String outputFile) throws IOException
	{
		InputStream fis = new BufferedInputStream(new FileInputStream(new File(inputFile)));		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outputFile)));				
		LanguageTransliterator lst = new LanguageTransliterator(fis, out);						
		lst.start();		
		out.close();
		fis.close();
	}	
}


