package com.pmc.aws.client;

import com.pmc.fw.resources.Resource;
import com.pmc.fw.resources.ResourceProvider;

public class ResourceProviderWrapper 
{
	public static final String AWSClientResource = "AWSClientResource";
	
	public static AWSClientResource getAWSClientResources()
	{
		Resource r = ResourceProvider.getResource(AWSClientResource);
		return ((AWSClientResource)r);
	}
}
