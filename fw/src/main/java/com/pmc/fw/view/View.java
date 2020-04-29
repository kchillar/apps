package com.pmc.fw.view;

import java.util.List;
import java.util.Map;

import com.pmc.fw.model.ResponseCode;

public interface View 
{
	public ResponseCode init(ViewConfig viewConfig, List<ViewConfig>childList, Map<String, View> appViewsMap, ViewEventHandler viewEventHandler);
	public void startInteraction();		
}
