package com.pmc.fw.view;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="app-ui-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppUIConfig 
{
	@XmlElement(name="event-handler-class")
	private String eventHandlerClassname;
	
	@XmlElement(name="view-impl-class")
	private String viewImplClass;
	
	@XmlElement(name="view-config")
	private List<ViewConfig> views;

	public List<ViewConfig> getViews() {
		return views;
	}

	public void setViews(List<ViewConfig> views) {
		this.views = views;
	}

	public String getEventHandlerClassname() {
		return eventHandlerClassname;
	}

	public void setEventHandlerClassname(String handlerClassname) {
		this.eventHandlerClassname = handlerClassname;
	}

	public String getViewImplClass() {
		return viewImplClass;
	}

	public void setViewImplClass(String viewImplClass) {
		this.viewImplClass = viewImplClass;
	}
	
	
	
}
