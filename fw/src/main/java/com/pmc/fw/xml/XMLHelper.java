package com.pmc.fw.xml;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author kalyanc
 *
 */
public class XMLHelper
{
	private static Logger log = LoggerFactory.getLogger(XMLHelper.class);
	
	private static HashMap<String, JAXBContext> jaxbContextMap = new HashMap<String, JAXBContext>();
	private static HashMap<String, Marshaller> marshallerMap = new HashMap<String, Marshaller>();
	private static HashMap<String, Unmarshaller> unmarshallerMap = new HashMap<String, Unmarshaller>();

	public static Object getObjectFromFile(Class<?> objectClass, File file)
	{
		if (file != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Will read from file: " + file.getPath());
			}
		}

		InputStream inp = null;
		try
		{
			inp = getInputStream(file);
			return getObjectFromInputStream(objectClass, inp);
		}finally
		{
			closeStreams(inp, null);
		}
	}

	public static Object getObjectFromInputStream(Class<?> objectClass, InputStream inp)
	{
		Unmarshaller unmarshaller = unmarshallerMap.get(objectClass.getName());
		try
		{
			if (unmarshaller == null)
			{
				JAXBContext jc = jaxbContextMap.get(objectClass.getName());
				if (jc == null)
				{
					jc = JAXBContext.newInstance(objectClass);
					jaxbContextMap.put(objectClass.getName(), jc);
				}
				unmarshaller = jc.createUnmarshaller();
				unmarshallerMap.put(objectClass.getName(), unmarshaller);
			}
			Object object = unmarshaller.unmarshal(inp);
			return object;
		}
		catch (JAXBException jaxbe)
		{
			throw new IllegalStateException("Unable to get object of class: " + objectClass.getName() + " from file",
					jaxbe);
		}
	}

	public static void writeObjectToOutputStream(Class<?> objectClass, Object object, OutputStream outStream)
	{
		Marshaller marshaller = marshallerMap.get(objectClass.getName());
		try
		{

			if (marshaller == null)
			{
				JAXBContext jc = jaxbContextMap.get(objectClass.getName());
				if (jc == null)
				{
					jc = JAXBContext.newInstance(objectClass);
					jaxbContextMap.put(objectClass.getName(), jc);
				}

				marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshallerMap.put(objectClass.getName(), marshaller);
			}

			marshaller.marshal(object, outStream);
		}
		catch (JAXBException jaxbe)
		{
			throw new IllegalStateException("Unable to store object of class: " + objectClass.getName() + " to file",
					jaxbe);
		}finally
		{
		}
	}

	public static void writeObjectToFile(Class<?> objectClass, Object object, File file)
	{
		if (file != null)
		{
			log.debug("Will write to file: " + file.getPath());
		}

		OutputStream os = getOutputStream(file);

		try
		{
			writeObjectToOutputStream(objectClass, object, os);
		}finally
		{
			closeStreams(null, os);
		}
	}

	private static InputStream getInputStream(File file)
	{
		try
		{
			if (file.isFile() && file.exists() && file.canRead())
			{
				FileInputStream fis = new FileInputStream(file);
				return fis;
			}
			else
			{
				log.warn("Unable to create input stream to file " + file.getPath() + ". Will return null");
				return null;
			}
		}
		catch (Exception exp)
		{
			log.error("Error", exp);
			throw new IllegalStateException("Error in loading file: " + file.getPath());
		}
	}

	private static OutputStream getOutputStream(File file)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			return fos;
		}
		catch (Exception exp)
		{
			log.error("Error", exp);
			throw new IllegalStateException("Error in loading file: " + file.getPath());
		}
	}

	public static void closeStreams(InputStream inp, OutputStream out)
	{
		if (inp != null)
		{
			try
			{
				inp.close();
			}
			catch (Exception exp)
			{
			}
			;
		}
		if (out != null)
		{
			try
			{
				out.close();
			}
			catch (Exception exp)
			{
			}
			;
		}
	}

}
