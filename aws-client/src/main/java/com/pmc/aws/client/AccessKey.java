package com.pmc.aws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="access-key")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessKey 
{
	@XmlAttribute(name="key-id")
	private String keyId;
	@XmlAttribute(name="s-key-id")
	private String sKeyId;
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getsKeyId() {
		return sKeyId;
	}
	public void setsKeyId(String sKeyId) {
		this.sKeyId = sKeyId;
	}
	
	
	
}
