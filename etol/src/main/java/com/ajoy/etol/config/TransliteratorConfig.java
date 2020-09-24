package com.ajoy.etol.config;

import java.util.ArrayList;
import java.util.List;

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
	
	@XmlAttribute(name="modifier")
	private String modifier;	

	@XmlElementWrapper(name="vowel-sound-chars")
	@XmlElement(name="char")
	private List<String> vowelCharList;
	
	@XmlElementWrapper(name="consonant-sound-chars")
	@XmlElement(name="char")
	private List<String> consonantCharList;

	@XmlElementWrapper(name="char-sequence-mappings")
	@XmlElement(name="char-sequence-mapping")
	private ArrayList<CharSequenceToCodePointMapping> symbolsList;
				
	public String getModifier() 
	{
		return modifier;
	}
	
	public void setModifier(String modifier) 
	{
		this.modifier = modifier;
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


	public String toString()
	{
		StringBuilder buff = new StringBuilder();		
		buff.append("{ language: "+language+", modifier: "+modifier+", symbolList:[");		
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
