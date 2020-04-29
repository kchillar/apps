package com.pmc.fw.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;

public class ViewEventHandlerImpl implements ViewEventHandler
{
	public static Logger log = LoggerFactory.getLogger(ViewEventHandlerImpl.class);
	
	@Override
	public ResponseCode  handleEvent(ViewEvent viewEvent)
	{
		ResponseCode code = new ResponseCode();
		log.info("Handled event: "+viewEvent);
		return code;
	}

}
