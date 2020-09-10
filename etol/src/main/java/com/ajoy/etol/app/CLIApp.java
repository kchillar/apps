package com.ajoy.etol.app;

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
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.EnglishTransliterator;
import com.ajoy.etol.LanguageTransliterator;
import com.ajoy.etol.mapper.DefaultCharSequenceMapper;

/**
 * 
 * @author kalyanc
 *
 */
public class CLIApp 
{
	private static Logger log = LogManager.getLogger(CLIApp.class);
	
	public static void main(String[] args) throws Exception
	{
		String outputFile = "e2l-out.html";
		
		if(args.length == 0)
		{
			System.out.println("Will transliterate from System.in and output to : "+outputFile);
			interativeTransliteration(outputFile);
		}		
		else if(args.length == 1)
		{
			outputFile = args[0];
			System.out.println("Will transliterationg from System.in and output to : "+outputFile);
			interativeTransliteration(outputFile);
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
	
	public static void interativeTransliteration(String outputFile) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pr = new PrintWriter(new File(outputFile));
		DefaultCharSequenceMapper mapper = new DefaultCharSequenceMapper();
		mapper.setTransliterationMarkedup(false);
		EnglishTransliterator et = new EnglishTransliterator(mapper);
		while(true)
		{
			System.out.print("e2l> ");
			String line = br.readLine();
			String out = et.transliterateString(line);
			pr.println(out);
			System.out.println(out);
			pr.flush();
		}
	}
		
	public static void processEtoLFiles(String inputFile, String outputFile) throws IOException
	{
		System.out.println("Starting E2L Processing from input: "+inputFile+" and will output to : "+outputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))));
		
		//DefaultCharSequenceMapper mapper = new DefaultCharSequenceMapper();
		
		EnglishTransliterator lst = new EnglishTransliterator();						
		lst.transliterateStream(br, wr);				
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


