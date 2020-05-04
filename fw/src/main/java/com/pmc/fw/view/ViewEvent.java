package com.pmc.fw.view;

import java.util.Map;

public interface ViewEvent 
{
	public long getUUId();
	public String getViewId();
	public View getView();
	public String getEventId();
	public Map<String, Object> getEventData();
	
}
