package com.ajoy.etol.app;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.Transliterator;
import com.ajoy.etol.mapper.CharSequenceMapperProvider;

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
		CharSequenceMapperProvider.init();
		
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
				//processLtoEFiles(args[0], args[1]);
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
		Transliterator et = Transliterator.getInstance(Transliterator.LANGUAGE_TELUGU);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		for(String fontFamily: ge.getAvailableFontFamilyNames())
			System.out.println("FontFamilyName: "+fontFamily);

		for(Font font: ge.getAllFonts())
			System.out.println("Name: "+font.getName()+" NumGlyphs: "+font.getNumGlyphs()+" fontName:"+font.getFontName()+" fontFamily:"+font.getFamily());

		
		
		while(true)
		{
			System.out.print("e2l> ");
			String line1 = br.readLine();
			String out   = et.toLanguageString(line1);
			String line2 = et.toPhoneticString(out);
			
			System.out.println("|"+line1+"|");
			System.out.println("|"+out+"|");
			System.out.println("|"+line2+"|");
									
			pr.println(out);
			pr.flush();
		}
		
		//pr.close();
	}
		
	public static void processEtoLFiles(String inputFile, String outputFile) throws IOException
	{
		System.out.println("Starting E2L Processing from input: "+inputFile+" and will output to : "+outputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		PrintWriter wr = new PrintWriter(new File(outputFile));		
		Transliterator lst = Transliterator.getInstance(Transliterator.LANGUAGE_TELUGU);
		String line;
		
		while( (line = br.readLine()) != null)
		{
			String out = lst.toLanguageString(line);
			wr.println(out);			
		}
						
		wr.close();
		br.close();
	}

	/*
	public static void processLtoEFiles(String inputFile, String outputFile) throws IOException
	{
		InputStream fis = new BufferedInputStream(new FileInputStream(new File(inputFile)));		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outputFile)));				
		LanguageTransliterator lst = new LanguageTransliterator(fis, out);						
		lst.start();		
		out.close();
		fis.close();
	}
	*/	
}


