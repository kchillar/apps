package com.pmc.aws.client.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util 
{
	public static Logger log = LoggerFactory.getLogger(Util.class);

	public static String getStringFromFile(String fileName)
	{
		String line = "";
		
		try
		{
			File file = new File(fileName);			
			log.info("reading from file: "+file.getPath());
			BufferedReader br = new BufferedReader(new FileReader(file));			
			StringBuilder buff = new StringBuilder();
			while( (line=br.readLine()) != null)
				buff.append(line);
			br.close();
			return buff.toString();			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		return line;
	}
}
