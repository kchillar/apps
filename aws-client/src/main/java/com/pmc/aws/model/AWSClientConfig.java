package com.pmc.aws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="aws-client-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class AWSClientConfig 
{
	@XmlElement(name="access-key")
	private AccessKey accessKey;
	
	@XmlElement(name="region")
	private RegionConfig regionConfig;

	public AccessKey getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(AccessKey accessKey) {
		this.accessKey = accessKey;
	}

	public RegionConfig getRegionConfig() {
		return regionConfig;
	}

	public void setRegionConfig(RegionConfig regionConfig) {
		this.regionConfig = regionConfig;
	}
	
	
	
}
