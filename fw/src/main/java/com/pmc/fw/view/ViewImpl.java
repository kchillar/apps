package com.pmc.fw.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;

/**
 * 
 * @author kalyanc
 *
 */
public abstract class ViewImpl implements View
{
	public static Logger log = LoggerFactory.getLogger(ViewImpl.class);	

	private List<ViewConfig> childList;	
	private Map<String, View> viewMap ;	
	private ViewEventHandler viewHandler;

	public ViewImpl()
	{		
	}
		
	public ResponseCode init(ViewConfig vInfo, List<ViewConfig> childList, Map<String, View> viewMap, ViewEventHandler viewHandler)
	{		
		ResponseCode code = new ResponseCode();
		this.setViewMap(viewMap);
		this.setChildList(childList);		
		this.viewHandler = viewHandler;
		code.setSuccess(true);
		return code;
	}
			
	
	public abstract void startInteraction() ;
		
	protected List<ViewConfig> getChildList() {
		return childList;
	}

	protected void setChildList(List<ViewConfig> childList) {
		this.childList = childList;
	}

	protected Map<String, View> getViewMap() {
		return viewMap;
	}

	protected void setViewMap(Map<String, View> viewMap) {
		this.viewMap = viewMap;
	}

	protected ViewEventHandler getViewHandler()
	{
		return viewHandler;
	}

	protected void setViewHandler(ViewEventHandler viewHandler)
	{
		this.viewHandler = viewHandler;
	}

}

