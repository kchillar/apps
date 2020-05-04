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
	@XmlAttribute(name="parent-id")
	private String parentId;
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="label")
	private String Label;
	
	@XmlAttribute(name="not-leaf")
	private boolean isNotLeafView = false;
	
	@XmlAttribute(name="view-class")
	private String viewClass;
	
	@XmlAttribute(name="event-id")
	private String eventId;
	
	@XmlElementWrapper(name="inputs")
	@XmlElement(name="param")
	private List<Param> inputParams;
	
	
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String toString()
	{
		return "{id:\""+getId()+"\", Label:\""+getLabel()+"\"}";
	}
}

