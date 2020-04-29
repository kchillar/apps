package com.pmc.fw.resources;

import java.util.HashMap;
import java.util.Map;

public final class ResourceProvider 
{
	private final Map<String, Resource> objectMap = new HashMap<>();
	
	private static final ResourceProvider singleton = new ResourceProvider();
	
	public static Resource getResource(String resourceId)
	{
		return singleton.getObjectMap().get(resourceId);
	}

	public static void addResource(String resourceId, Resource resource)
	{
		singleton.getObjectMap().put(resourceId, resource);
	}
	
	private Map<String, Resource> getObjectMap()
	{
		return objectMap;
	}
}
