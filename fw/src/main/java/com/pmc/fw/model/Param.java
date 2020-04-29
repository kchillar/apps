package com.pmc.fw.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="param")
@XmlAccessorType(XmlAccessType.FIELD)
public class Param 
{
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="value")
	private String value;
	@XmlAttribute(name="type")
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString()
	{
		return "{n:"+getName()+" v:"+getValue()+" t:"+getType()+"}";
	}
}
