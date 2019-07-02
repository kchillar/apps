package com.yoo.etol.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="transliterator-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransliteratorConfig 
{
	@XmlAttribute(name="is-transliteration-markedup")
	private boolean transliterationMarkedup = false;
	@XmlElement(name="markup-start")
	private String markupStart;
	@XmlElement(name="markup-end")
	private String markupEnd;

	
	@XmlAttribute(name="language")
	private String language;
	
	@XmlAttribute(name="visarga")
	private String visarga;	

	@XmlElementWrapper(name="vowel-sound-chars")
	@XmlElement(name="char")
	private List<String> vowelCharList;
	
	@XmlElementWrapper(name="consonant-sound-chars")
	@XmlElement(name="char")
	private List<String> consonantCharList;

	@XmlElementWrapper(name="char-sequence-mappings")
	@XmlElement(name="char-sequence-mapping")
	private ArrayList<CharSequenceToCodePointMapping> symbolsList;
				
	public String getVisarga() 
	{
		return visarga;
	}
	
	public void setVisarga(String visarga) 
	{
		this.visarga = visarga;
	}
	
	public List<String> getVowelCharList() {
		return vowelCharList;
	}

	public void setVowelCharList(List<String> vowelCharList) {
		this.vowelCharList = vowelCharList;
	}
	
	public List<String> getConsonantCharList() {
		return consonantCharList;
	}

	public void setConsonantCharList(List<String> consonantCharList) {
		this.consonantCharList = consonantCharList;
	}

	public ArrayList<CharSequenceToCodePointMapping> getSymbolsList() {
		return symbolsList;
	}


	public void setSymbolsList(ArrayList<CharSequenceToCodePointMapping> symbolsList) 
	{
		this.symbolsList = symbolsList;
	}
		
	public String getLanguage() 
	{
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getMarkupStart() {
		return markupStart;
	}
	
	public void setMarkupStart(String markupStart) {
		this.markupStart = markupStart;
	}

	public String getMarkupEnd() {
		return markupEnd;
	}

	
	public void setMarkupEnd(String markupEnd) {
		this.markupEnd = markupEnd;
	}

	public boolean isTransliterationMarkedup() {
		return transliterationMarkedup;
	}

	
	public void setTransliterationMarkedup(boolean transliterationMarkedup) 
	{
		this.transliterationMarkedup = transliterationMarkedup;
	}



	public static TransliteratorConfig getFromStream(InputStream in)
			throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(TransliteratorConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TransliteratorConfig info = (TransliteratorConfig) jaxbUnmarshaller.unmarshal(in);
		return info;
	}

	public static void writeToStream(OutputStream out, TransliteratorConfig info)
			throws JAXBException {			
		JAXBContext jaxbContext = JAXBContext.newInstance(TransliteratorConfig.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		jaxbMarshaller.marshal(info, out);			
	}

	public String toString()
	{
		StringBuilder buff = new StringBuilder();		
		buff.append("{ language: "+language+", visarga: "+visarga+", symbolList:[");		
		if(getSymbolsList()!= null)
		{
			for(CharSequenceToCodePointMapping sy: getSymbolsList())			
				buff.append(sy.toString()+", "+"\n");			
		}		
		buff.append("],\n");
		buff.append("vowelSoundChars:[");
		
		if(getVowelCharList() != null)
		{
			for(int i=0; i<getVowelCharList().size(); i++)
				buff.append("'"+getVowelCharList().get(i).charAt(0)+"', ");
		}
		buff.append("],\n");
		buff.append("consonantSoundChars:[");
		if(getConsonantCharList() != null)
		{
			for(int i=0; i<getConsonantCharList().size(); i++)
				buff.append("'"+getConsonantCharList().get(i).charAt(0)+"', ");
		}

		buff.append("]}");
		return buff.toString();
	}
	
}
