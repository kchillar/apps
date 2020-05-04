package com.pmc.fw.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="view-initializer-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewInitializerConfig 
{
	@XmlAttribute(name="view-config-path")
	private String viewConfigPath = "./views";
	
	@XmlAttribute(name="default-view-config-filename")
	private String viewConfigFilename = "view-config.xml";

	public String getViewConfigPath() {
		return viewConfigPath;
	}

	public void setViewConfigPath(String viewConfigPath) {
		this.viewConfigPath = viewConfigPath;
	}

	public String getViewConfigFilename() {
		return viewConfigFilename;
	}

	public void setViewConfigFilename(String viewConfigFilename) {
		this.viewConfigFilename = viewConfigFilename;
	}
	
	
}
