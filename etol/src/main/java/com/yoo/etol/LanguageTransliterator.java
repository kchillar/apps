package com.yoo.etol;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yoo.etol.config.CharSequenceToCodePointMapping;
import com.yoo.etol.config.Settings;
import com.yoo.etol.mapper.DefaultCharSequenceMapper;
import com.yoo.etol.mapper.LanguageToEnglishCharSequenceMapper;

public class LanguageTransliterator 
{
	private static Logger log = LogManager.getLogger(LanguageTransliterator.class);
	private LanguageToEnglishCharSequenceMapper mapper;
	private InputStreamReader inStream;	
	private OutputStream outStream;
	private Writer wr;

	public LanguageTransliterator(InputStream iStream, OutputStream oStream, LanguageToEnglishCharSequenceMapper language)	
	{		
		inStream = new InputStreamReader(iStream);
		outStream = oStream;
		this.mapper = language;
	}
	
	public LanguageTransliterator(InputStream iStream, OutputStream oStream)
	{	
		this.inStream = new InputStreamReader(iStream);
		
		outStream = oStream;
		mapper = new DefaultCharSequenceMapper();	
	}
	
	private CharSequenceToCodePointMapping prevPrevSymbol = null;
	private CharSequenceToCodePointMapping prevSymbol = null; 
	
	public void start() throws IOException 
	{								
		long startTime = System.currentTimeMillis();
		wr = new OutputStreamWriter(outStream, Settings.Encoding);
		//wr = new OutputStreamWriter(outStream);
		
		log.debug("VISARGA int: "+mapper.getVisarga());
		
		try
		{
			startTime = System.currentTimeMillis();
			do
			{	
			  int val = inStream.read();					
			  if(val == -1)
				  break;				
			  			  
			  CharSequenceToCodePointMapping sy = mapper.getAsciSequence(val);
			  
			  if(sy == null)
			  {
				  log.debug("asci : '"+((char)val)+"' prevPrev:"+prevPrevSymbol+" prev:"+prevSymbol+" sy:"+sy);
				  
				  if( (prevSymbol!= null) && (prevSymbol.getType()== CharSequenceToCodePointMapping.Consonant))
				  {
					  log.debug("APPENDING 1 a");
						  wr.write('a');
				  }

				  wr.write(val);
			  }
			  else
			  {
				  log.debug("lang : '"+((char)val)+"' prevPrev: "+prevPrevSymbol+" prev: "+prevSymbol+" sy: "+sy);
				  
				  
				  if( (prevSymbol!= null) && (prevSymbol.getType()== CharSequenceToCodePointMapping.Consonant) &&
						  (sy.getType() == CharSequenceToCodePointMapping.Consonant))
				  {
					  log.debug("APPENDING 2 a");
						  wr.write('a');
				  }
				  

				  
				  if(sy != mapper.getVisargaSymbol())
				  {
					  //log.debug(sy.getAsciiCharSequence()+" = "+val);
					  wr.write(sy.getAsciiCharSequence()+"");
				  }
				  else
				  {
					  log.debug("Visarga");
				  }
			  }
			  
			  prevPrevSymbol = prevSymbol;
			  prevSymbol = sy;
			}
			while(true);
			long endTime = System.currentTimeMillis();
			System.out.println("Took: "+ (endTime-startTime) +" millis secods. ");
		}
		finally
		{
			try
			{
				if(wr != null)
				{
					wr.flush();
					wr.close();
				}				
			}
			catch(Exception io)
			{
				io.printStackTrace();
			}
		}
	}

}
