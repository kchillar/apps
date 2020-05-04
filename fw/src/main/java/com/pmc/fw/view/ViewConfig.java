package com.pmc.fw.view;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.pmc.fw.model.Param;

@XmlRootElement(name="view-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewConfig
{
	@XmlAttribute(name="id")
	private String id = "";
	@XmlAttribute(name="label")
	private String Label;
	
	@XmlAttribute(name="not-leaf")
	private boolean isNotLeafView = false;
	
	@XmlElement(name="view-class")
	private String viewClass;

	@XmlElement(name="event-handler-class")
	private String eventHandlerClassname;
		
	@XmlAttribute(name="event-id")
	private String eventId;
	
	@XmlElementWrapper(name="inputs")
	@XmlElement(name="param")
	private List<Param> inputParams;
		
	@XmlAttribute(name="view-dir")
	private String viewDir;
	
	@XmlElement(name="view-config")
	private List<ViewConfig> childViewConfigs;
 	
	public ViewConfig()
	{		
	}
	
	public ViewConfig(String id, String desc)
	{
		setId(id);
		setLabel(desc);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String desc) {
		this.Label = desc;
	}

	
	public boolean isNotLeafView() {
		return isNotLeafView;
	}

	public void setNotLeafView(boolean isLeafView) {
		this.isNotLeafView = isLeafView;
	}

	public List<Param> getInputParams() {
		return inputParams;
	}

	public void setInputParams(List<Param> inputParams) {
		this.inputParams = inputParams;
	}

	
	public String getViewClass() {
		return viewClass;
	}

	public void setViewClass(String viewClass) {
		this.viewClass = viewClass;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
		
	

	public String getViewDir() {
		return viewDir;
	}

	public void setViewDir(String viewDir) {
		this.viewDir = viewDir;
	}

	public String getEventHandlerClassname() {
		return eventHandlerClassname;
	}

	public void setEventHandlerClassname(String eventHandlerClassname) {
		this.eventHandlerClassname = eventHandlerClassname;
	}

	
	
	public List<ViewConfig> getChildViewConfigs() {
		return childViewConfigs;
	}

	public void setChildViewConfigs(List<ViewConfig> childViewConfigs) {
		this.childViewConfigs = childViewConfigs;
	}

	public String toString()
	{
		return "{id:\""+getId()+"\", Label:\""+getLabel()+"\"}";
	}
}

