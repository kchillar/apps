package com.pmc.aws.client;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;

public class AWSViewEventHandler implements ViewEventHandler 
{
	@Override
	public ResponseCode handleEvent(ViewEvent viewEvent) 
	{
		ResponseCode code = new ResponseCode();
		return code;
	}

}
