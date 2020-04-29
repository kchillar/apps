package com.pmc.fw.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="resource-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceConfig 
{
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="resource-class")
	private String resourceClass;
	
	@XmlAttribute(name="config-file")
	private String configFile;

	public String toString()
	{
		return "{id:"+getId()+", resource-class:"+getResourceClass()+" config-file: "+getConfigFile()+"}";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceClass() {
		return resourceClass;
	}

	public void setResourceClass(String resourceClass) {
		this.resourceClass = resourceClass;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	
}
