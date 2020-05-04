package com.pmc.fw.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.resources.Resource;
import com.pmc.fw.xml.XMLHelper;

public class ViewInitializer implements Resource
{
	private static Logger log = LoggerFactory.getLogger(ViewInitializer.class);
	
	private Map<String, ViewConfig> viewConfigMap = new HashMap<>();	
	private Map<String, View> viewMap = new HashMap<>();	
	private ViewConfig rootViewConfig;
	private View rootView;		
		
	public ViewInitializer()
	{		
	}
	
	@Override
	public ResponseCode init(InputStream configStream) 
	{
		ResponseCode code = new ResponseCode();
		
		try
		{
			ViewInitializerConfig viConfig = (ViewInitializerConfig) XMLHelper.getObjectFromInputStream(ViewInitializerConfig.class, configStream);			
			File dir = new File(viConfig.getViewConfigPath());
			rootViewConfig = getViewConfig(dir);
			this.processConfig(rootViewConfig, viewConfigMap, viewMap, dir);
			code.setSuccess(true);			
			rootView.startInteraction();
		}
		catch(Exception exp)
		{
			log.error("Error in init", exp);
		}
		return code;
	}

	
	public void init(String viewBaseDir) throws Exception
	{
		File dir = new File(viewBaseDir);
		rootViewConfig = getViewConfig(dir);
		this.processConfig(rootViewConfig, viewConfigMap, viewMap, dir);
	}
	
	
	private ViewConfig getViewConfig(File inDir) throws IOException
	{
		FileInputStream fis = new FileInputStream(new File(inDir,"view-config.xml" ));		
		ViewConfig vc = (ViewConfig) XMLHelper.getObjectFromInputStream(ViewConfig.class, fis);
		fis.close();
		return vc;
	}
	
	private void processConfig(ViewConfig viewConfig, Map<String, ViewConfig> viewConfigMap, Map<String, View> viewMap , File parentDir) throws Exception
	{
		log.info("processConfig() start: "+parentDir.getPath());
						
		ViewEventHandler viewHandler = null;
		View view = null;
		
		if(viewConfig.getEventHandlerClassname() != null)
			viewHandler = (ViewEventHandler) Class.forName(viewConfig.getEventHandlerClassname()).newInstance();
				
		view = createNewView(viewConfig);						
		view.init(viewConfig, viewConfigMap, viewMap, viewHandler);
		viewConfigMap.put(viewConfig.getId(), viewConfig);
		viewMap.put(viewConfig.getId(), view);
		
		log.info("***** stored view by id: "+viewConfig.getId()+" ****** ");
		
		if(rootView == null)		
			rootView = view;
				
		if(viewConfig.getChildViewConfigs() != null)
		{
			int i =0 ;
			for(ViewConfig childConfig: viewConfig.getChildViewConfigs())
			{
				String fullId = viewConfig.getId() +"/" + i;
				childConfig.setId(fullId);
								
				if(childConfig.getViewDir() != null)
				{
					File dir = new File(parentDir, childConfig.getViewDir());
					ViewConfig vc = getViewConfig(dir);
					vc.setId(childConfig.getId());
					vc.setLabel(childConfig.getLabel());
					this.processConfig(vc,  viewConfigMap, viewMap, dir);
				}
				else
				{
					ViewEventHandler childHandler = null;
					
					if(childConfig.getEventHandlerClassname() != null)
						childHandler = (ViewEventHandler) Class.forName(childConfig.getEventHandlerClassname()).newInstance();
					
					if(childHandler == null)
						childHandler = viewHandler;
					
					View childView = createNewView(childConfig);
					childView.init(childConfig, viewConfigMap, viewMap, childHandler);
					viewConfigMap.put(childConfig.getId(), childConfig);
					viewMap.put(childConfig.getId(), childView);
				}
				
				i++;
			}
		}		
		
		log.info("processConfig() end: "+parentDir.getPath());		
	}
	
	
	private View createNewView(ViewConfig vc) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		View view ;
		if(vc.getViewClass() != null)		
			view = (View) Class.forName(vc.getViewClass()).newInstance();
		else
			view = (View) Class.forName(rootViewConfig.getViewClass()).newInstance();
		return view;
	}
	
	public void start()
	{		
		log.info("main view start()");
		rootView.startInteraction();
		log.info("main view end()");		
	}
	
	public static void main(String[] args) throws Exception
	{
		ViewInitializer vi = new ViewInitializer();
		vi.init(args[0]);
		vi.start();
	}

}
