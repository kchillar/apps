package com.pmc.fw.view;

import java.util.Map;

import com.pmc.fw.model.ResponseCode;

public interface View 
{
	public ResponseCode init(ViewConfig viewConfig, Map<String, ViewConfig> viewConfigMap, Map<String, View> appViewsMap, ViewEventHandler viewEventHandler);
	public void startInteraction();		
}
