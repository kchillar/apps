package com.ajoy.etol.app;

import java.io.Reader;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemReaderWorker implements Runnable
{
	private static Logger log = LogManager.getLogger(SystemReaderWorker.class);
	
	private Writer writer;
	private Reader reader;
	
	public SystemReaderWorker(Reader reader, Writer os)
	{
		this.writer = os;
		this.reader = reader;
	}
	
	public void run()
	{
		log.info("run() start");
		try
		{
			log.info("Will read from standinput and output to writer");
			do
			{
				int val = reader.read();								
				log.debug("> "+((char)val));
				writer.write(val);
				if(val < 0)
					break;
			}
			while(true);
		}
		catch(Exception exp)
		{
			log.error("Error in reader", exp);
		}
		
		log.info("run() end");
	}
}
