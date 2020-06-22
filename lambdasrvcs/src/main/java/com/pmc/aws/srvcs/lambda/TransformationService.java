package com.pmc.aws.srvcs.lambda;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class TransformationService  implements RequestStreamHandler 
{
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
	{
		LambdaLogger logger = context.getLogger();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
		
		try
		{
			Response res = new Response();
			Request msg = gson.fromJson(reader, Request.class);
			String inputData = msg.getInputMsg();
			String outputData = gson.toJson(res);
			
			logger.log("Got input: "+inputData+" Gave: "+outputData);
			
			writer.write(outputData);
			
			if (writer.checkError())			
				logger.log("WARNING: Writer encountered an error.");			
		}
		catch (IllegalStateException | JsonSyntaxException exception)
		{
			logger.log(exception.toString());
		}
		finally
		{
			reader.close();
			writer.close();
		}
	}
}

class Response
{
	private boolean success = true;
	private String code;
	private String msg;
	private String responseData="{}";
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
	
}

class Request
{
	private String workflowName;
	private String clientId;
	private String appId;
	private String version;
	
	private String inputMsg;	
	

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInputMsg() {
		return inputMsg;
	}

	public void setInputMsg(String inputMsg) {
		this.inputMsg = inputMsg;
	}

	
}

