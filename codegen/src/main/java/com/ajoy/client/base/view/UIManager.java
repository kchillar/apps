package com.ajoy.client.base.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.Node;

/**
 * 
 * @author kalyanc
 *
 */
public class UIManager 
{
	private static Logger log = LogManager.getLogger(UIManager.class);	
	private static List<String> idList = new ArrayList<>();
	private static Map<String, FXViewBuilderInfo> FXViewBuilerConfigMap = new HashMap<>();	
	private static Map<String, FXViewBuilder> viewBuilderMap = new HashMap<>();

	public static Node buildUI(FXViewBuilderInfo config)
	{
		return createView(config, "", FXViewBuilerConfigMap, viewBuilderMap, idList);
	}
	
	private static Node createView(FXViewBuilderInfo config, String parentId, Map<String, FXViewBuilderInfo> configMap, Map<String, FXViewBuilder> builderMap, List<String> idList ) 
	{
		try
		{
			if(config == null)			
				throw new IllegalStateException("Invalid ViewConfig: "+null);

			if(config.getViewBuilderClassname() != null)
			{
				if(log.isDebugEnabled())
					log.debug("using class: "+config.getViewBuilderClassname()+" to create view: "+config.getName());

				Class<?> aClass =  Class.forName(config.getViewBuilderClassname());
				FXViewBuilder builder = (FXViewBuilder) aClass.newInstance();	
				
				String myId = parentId+FXViewBuilder.PathSeperator+config.getName(); 
				
				config.setId(myId);
				configMap.put(myId, config);
				idList.add(myId);
				
				List<Node> childViewList = new ArrayList<Node>();
				List<String> childNodeIds = new ArrayList<String>();
				
				if(config.getChildViewConfigList() != null && config.getChildViewConfigList().size() > 0)
				{
					for(FXViewBuilderInfo childConfig: config.getChildViewConfigList())
					{
						String childPath = myId+FXViewBuilder.PathSeperator+childConfig.getName();
						Node childView = createView(childConfig, myId, configMap, builderMap, idList);
						log.info(childPath+" view : "+childView);
						
						childNodeIds.add(childPath);
						childViewList.add(childView);						
					}
				}
				
				builderMap.put(myId, builder);
				Node view = builder.createFXView(config);
				return view;
			}
			else
			{
				log.warn("No view-classname is provided for view :"+config.getName()+", will return null");
				return null;
			}
		}
		catch(Exception exp)
		{
			log.error("Error in creating View instance", exp);
			return null;
		}
	}
		
	public static FXViewBuilder getFXViewBuilder(String viewPath)
	{
		FXViewBuilder builder =  viewBuilderMap.get(viewPath);
		if(builder == null)
			log.warn("No FXviewBuilder found for path: "+viewPath);
		else
			log.info("Found builder for path: "+viewPath);
			
		return builder;
	}
}
