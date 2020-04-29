package com.pmc.fw.view;

import com.pmc.fw.model.ResponseCode;

public interface ViewEventHandler 
{
	public ResponseCode handleEvent(ViewEvent event);
}
