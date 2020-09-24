package com.ajoy.etol.config;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="char-sequence-mapping")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharSequenceToCodePointMapping 
{
	//Consonant modifier
	public static final int  Modifier = 0;
	public static final int  Hallu = 1;
	public static final int  Acchu = 2;
	public static final int  Gunintam = 3;
	public static final int  CommentChar = 4;
	public static final int  Other = 5;
	
	@XmlAttribute(name="hex-codepoint")
	private String hexCodepoint;
	@XmlAttribute(name="ascii-char-sequence")
	private String asciiCharSequence;
	@XmlAttribute(name="type")
	private int type;		
	@XmlAttribute(name="anunasika-or-visarga")
	private boolean anunasiakOrVisarga;
	
	//This is set and does not come from XML
	private int codepoint;
	
	public String getHexCodepoint()
	{
		return hexCodepoint;
	}
	
	public int getCodepoint()
	{
		return codepoint;
	}
	
	public void setCodepoint(int codepoint)
	{
		this.codepoint = codepoint;
	}
	
	
	public void setHexCodepoint(String hexString) 
	{
		this.hexCodepoint = hexString;
	}
	public String getAsciiCharSequence() {
		return asciiCharSequence;
	}
	
	
	public void setAsciiCharSequence(String asciiCharSequence) {
		this.asciiCharSequence = asciiCharSequence;
	}
	
	public int getType() {
		return type;
	}
	

	public void setType(int type) 
	{
		this.type = type;
	}
	
	public boolean isAnunasiakOrVisarga() {
		return anunasiakOrVisarga;
	}

	public void setAnunasiakOrVisarga(boolean anunasiakOrVisarga) {
		this.anunasiakOrVisarga = anunasiakOrVisarga;
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
