package com.ajoy.etol.config;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="char-sequence-mapping")
public class CharSequenceToCodePointMapping 
{
	public static final int  Visarga = 0;
	public static final int  Consonant = 1;
	public static final int  Vowel = 2;
	public static final int  ConsonantModification = 3;
	public static final int  CommentChar = 4;
	public static final int  Other = 5;
	
	//
	private String hexCodepoint;
	private String asciiCharSequence;
	private int type;	
	private int[] codepoints;
	
	public String getHexCodepoint()
	{
		return hexCodepoint;
	}
	
	public int[] getCodepoints()
	{
		return codepoints;
	}
	
	public void setCodepoints(int[] codepoints)
	{
		this.codepoints = codepoints;
	}
	
	@XmlAttribute(name="hex-codepoint")
	public void setHexCodepoint(String hexString) 
	{
		this.hexCodepoint = hexString;
	}
	public String getAsciiCharSequence() {
		return asciiCharSequence;
	}
	
	@XmlAttribute(name="ascii-char-sequence")
	public void setAsciiCharSequence(String asciiCharSequence) {
		this.asciiCharSequence = asciiCharSequence;
	}
	
	public int getType() {
		return type;
	}
	
	@XmlAttribute(name="type")
	public void setType(int type) 
	{
		this.type = type;
	}
	

	public static CharSequenceToCodePointMapping getFromStream(InputStream in)
			throws JAXBException 
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(CharSequenceToCodePointMapping.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		CharSequenceToCodePointMapping info = (CharSequenceToCodePointMapping) jaxbUnmarshaller.unmarshal(in);
		return info;
	}

	public static void writeToStream(OutputStream out, CharSequenceToCodePointMapping info)
			throws JAXBException
	{			
		JAXBContext jaxbContext = JAXBContext.newInstance(CharSequenceToCodePointMapping.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		jaxbMarshaller.marshal(info, out);			
	}

	public String toString()
	{
		StringBuilder buff = new StringBuilder(); 		
		buff.append("{asci:"+asciiCharSequence+", hex:"+hexCodepoint+", type: "+type+"}"); 		
		return buff.toString();
	}
}
