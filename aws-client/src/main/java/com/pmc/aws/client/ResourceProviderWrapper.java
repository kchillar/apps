package com.pmc.aws.client;

import com.pmc.fw.resources.Resource;
import com.pmc.fw.resources.ResourceProvider;

public class ResourceProviderWrapper 
{
	public static final String AWSClientResource = "AWSClientResource";
	
	public AWSClientResource getAWSViewHandler()
	{
		Resource r = ResourceProvider.getResource(AWSClientResource);
		return ((AWSClientResource)r);
	}
}
