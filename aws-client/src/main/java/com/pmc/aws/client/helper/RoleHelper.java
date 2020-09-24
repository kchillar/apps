package com.pmc.aws.client.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.aws.client.AWSClientResource;
import com.pmc.aws.client.ResourceProviderWrapper;

import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

public class RoleHelper 
{

	public static Logger log = LoggerFactory.getLogger(RoleHelper.class);

	public RoleHelper()
	{
	}

	private final LambdaClient getLambdaClient()
	{
		//AssumeRoleRequest request = new AssumeRoleRequest().withRoleArn("arn:aws:iam::123456789012:role/demo").withRoleSessionName("Bob");
		return null;
	}


			 
}
