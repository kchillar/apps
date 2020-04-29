package com.pmc.fw.resources;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="resource-initializer-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceInitializerConfig 
{
	@XmlElement(name="resource-config")
	private List<ResourceConfig> resourceConfigs;

	public List<ResourceConfig> getResourceConfigs() 
	{
		return resourceConfigs;
	}

	public void setResourceConfigs(List<ResourceConfig> resourceConfigs) 
	{
		this.resourceConfigs = resourceConfigs;
	}
}
