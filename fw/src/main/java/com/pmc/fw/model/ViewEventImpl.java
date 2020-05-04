package com.pmc.fw.model;

import java.util.Map;

import com.pmc.fw.view.View;
import com.pmc.fw.view.ViewEvent;

public class ViewEventImpl implements ViewEvent
{
	private long UUId;
	private String eventId;
	private String viewId;
	private View view;
	private Map<String, Object> eventData;
	
	public ViewEventImpl()
	{
		//I know need to be changed !!!
		UUId = System.currentTimeMillis();
	}

	public ViewEventImpl(String viewId, String eventId, View view, Map<String, Object> eventData)
	{
		UUId = System.currentTimeMillis();
		setViewId(viewId);
		setEventId(eventId);
		setView(view);
		setEventData(eventData);
	}
	
	public String toString()
	{
		return "{uuId:"+getUUId()+", viewId:"+getViewId()+", eventData:"+getEventData()+"}";
	}
	
	public long getUUId() {
		return UUId;
	}
	public void setUUId(long uUId) {
		this.UUId = uUId;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public Map<String, Object> getEventData() {
		return eventData;
	}
	public void setEventData(Map<String, Object> eventData) {
		this.eventData = eventData;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	
}
