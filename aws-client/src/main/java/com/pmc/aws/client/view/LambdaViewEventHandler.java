package com.pmc.aws.client.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;

public class LambdaViewEventHandler implements ViewEventHandler
{
	public static Logger log = LoggerFactory.getLogger(LambdaViewEventHandler.class);
	
	private static final String ListLambdas = "List-Lambdas";
	private static final String CreateLambda = "Create-Lambda";
	private static final String ExecuteLambda = "Execute-Lambda";

	@Override
	public ResponseCode handleEvent(ViewEvent event) 
	{
		ResponseCode code = new ResponseCode();

		String eventId = event.getEventId();
		
		log.info("Event: "+eventId+" params: "+event.getEventData());

		switch(eventId)
		{
		case ListLambdas:
			log.info("List S3 Buckets");			
			break;
		case CreateLambda:
			log.info("Create S3 bucket");
			break;
		case ExecuteLambda:
			log.info("List keys in a S3 bucket");
			break;
		default:
			log.info("default");
		}

		log.info("completed event handling");
		
		return code;
	}

}
