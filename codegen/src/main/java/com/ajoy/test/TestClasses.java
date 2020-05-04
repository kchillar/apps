package com.ajoy.test;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.ClassInfo;
import com.ajoy.model.codegen.Classes;
import com.ajoy.model.codegen.FieldInfo;
import com.ajoy.model.codegen.ResponseCode;
import com.ajoy.model.codegen.SessionInfo;
import com.ajoy.service.codegen.bo.ClasspathBO;
import com.ajoy.service.codegen.workflow.CallContext;

public class TestClasses 
{
	private static Logger log = LogManager.getLogger(TestClasses.class);
	
	public static void main(String[] args) throws SQLException
	{
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setSelectedProfile("Dev");

		CallContext context  = new CallContext();
		context.setSessionInfo(sessionInfo);
		
		ClasspathBO bo = new ClasspathBO();		
		ResponseCode<Classes> code = bo.getClasses(context);
		
		if(code.isSuccess())
		{
			if(code.getObject() != null)
			{
				for(String className: code.getObject().getClassInfoList())					
				{
					ClassInfo classInfo = new ClassInfo();
					classInfo.setName(className);
					
					ResponseCode<ClassInfo> aCode = bo.getClassDetails(classInfo, context);
					
					if(aCode.isSuccess())
					{
						log.info("["+className+"]");
						for(FieldInfo info : aCode.getObject().getFieldList())
						{
							log.info("| --> className: "+info.getClassname()+" "+info.getName());
						}
					}
					
					log.info("Classname: "+className);
				}
			}
		}
		else
		{
			log.info("Unable to find classes");
		}
	}

}
