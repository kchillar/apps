package com.pmc.fw.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.xml.XMLHelper;

public class ResourceInitializer  
{
	public static Logger log = LoggerFactory.getLogger(ResourceInitializer.class);
	private static final String ConfigFileName = "resource-initializer-config.xml";
	private ResourceInitializerConfig config;
	private static File baseDir;
		
	public ResourceInitializer(String baseDirName) throws IOException
	{
		baseDir = new File(baseDirName);
		File configFile = new File(this.baseDir,ConfigFileName);					
		config = (ResourceInitializerConfig) XMLHelper.getObjectFromFile(ResourceInitializerConfig.class, configFile);		
		init();
	}
	
	public static File getResourcesBaseDir()
	{
		return baseDir;
	}
	
	public void init()
	{
		try
		{
			for(ResourceConfig rConfig: config.getResourceConfigs())
			{
				try
				{
					Resource resource = (Resource) Class.forName(rConfig.getResourceClass()).newInstance();					
					File configFile = new File(baseDir, rConfig.getConfigFile());
					FileInputStream fis = new FileInputStream(configFile);					
					ResponseCode code = resource.init(fis);					
					if(!code.isSuccess())
						log.error("Error in creating resource: "+rConfig);
					ResourceProvider.addResource(rConfig.getId(), resource);
					fis.close();					
				}
				catch(Exception exp)
				{
					log.error("Exception in creating resource"+rConfig, exp);
				}
			}			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
	}

	public static void main(String[] args) throws IOException
	{
		ResourceInitializer ri = new ResourceInitializer(args[0]);
	}	
}
