package com.ajoy.etol.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="char-sequence-mapper-provider-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharSequenceMapperProviderConfig 
{	
	@XmlElement(name="language")
	private List<LanguageInfo> languageInfoList;

	public List<LanguageInfo> getLanguageInfoList() {
		return languageInfoList;
	}

	public void setLanguageInfoList(List<LanguageInfo> languageInfoList) 
	{
		this.languageInfoList = languageInfoList;
	}

	
	
}
