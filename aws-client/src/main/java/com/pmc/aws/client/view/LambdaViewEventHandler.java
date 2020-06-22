package com.pmc.aws.client.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.aws.client.helper.LambdaHelper;
import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;

public class LambdaViewEventHandler implements ViewEventHandler
{
	public static Logger log = LoggerFactory.getLogger(LambdaViewEventHandler.class);
	
	private static final String ListLambdas = "List-Lambdas";
	private static final String CreateLambda = "Create-Lambda";
	private static final String ExecuteLambda = "Execute-Lambda";
	private static final String DeleteLambda = "Delete-Lambda";

	private LambdaHelper lambdaClient = new LambdaHelper(); 
	
	@Override
	public ResponseCode handleEvent(ViewEvent event) 
	{
		ResponseCode code = new ResponseCode();

		String eventId = event.getEventId();
		
		log.info("Event: "+eventId+" params: "+event.getEventData());

		switch(eventId)
		{
		case ListLambdas:
			lambdaClient.listFunctions();
			break;
		case CreateLambda:
			lambdaClient.createLambdaFunction(event.getEventData());
			break;
		case ExecuteLambda:
			lambdaClient.executeLambdaFunction(event.getEventData());
			break;
		case DeleteLambda:
			lambdaClient.deleteLambdaFunction(event.getEventData());
			break;			
		default:
			log.info("default");
		}

		log.info("completed event handling");
		
		return code;
	}

}
