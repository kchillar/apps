package com.pmc.fw.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.resources.Resource;
import com.pmc.fw.xml.XMLHelper;

/**
 * 
 * @author kalyanc
 *
 */
public class AppUI implements Resource
{
	private static Logger log = LoggerFactory.getLogger(AppUI.class);
	private AppUIConfig viewConfig;
	
	private ArrayList<ViewConfig> viewInfoList = new ArrayList<>();
	private Map<String, ViewConfig> viewInfoMap = new HashMap<>();	
	
	private Map<String, ArrayList<ViewConfig>> viewChildren = new HashMap<>();
	private ViewEventHandler viewHandler;
	private String topMostViewId;
	private Map<String, View> viewMap = new HashMap<>();
		
	public AppUI()
	{		
	}
	
	@Override
	public ResponseCode init(InputStream configStream) 
	{
		ResponseCode code = new ResponseCode();
		
		try
		{
			this.viewConfig = (AppUIConfig) XMLHelper.getObjectFromInputStream(AppUIConfig.class, configStream);
			viewHandler = (ViewEventHandler) Class.forName(viewConfig.getEventHandlerClassname()).newInstance();
			this.init();
			this.createViewsAndHandlers();			
			this.start();
			code.setSuccess(true);			
		}
		catch(Exception exp)
		{
			log.error("Error in init", exp);
		}
		return code;
	}
	
	private void init()
	{
		log.info("init() start");
		
		if(viewConfig.getViews() != null)
		{
			for(ViewConfig vi: viewConfig.getViews())
			{
				if(viewInfoMap.get(vi.getId()) == null)
					viewInfoMap.put(vi.getId(), vi);
				else
					throw new IllegalStateException("Duplicate id: "+vi.getId());
				viewInfoList.add(vi);
				
				if(vi.getParentId() != null)
				{
					ArrayList<ViewConfig> list;					
					if( (list = viewChildren.get(vi.getParentId())) == null)
						viewChildren.put(vi.getParentId(), (list=new ArrayList<>()));
					list.add(vi);
				}
				else
				{
					if(topMostViewId !=null)
						throw new IllegalStateException("Views "+topMostViewId+" and "+vi.getId()+" both donot have parent. Only one can be the root view !!!")
;					topMostViewId = vi.getId();
					log.info("viewId: "+topMostViewId+" is the top most view");
				}
			}
		}		
		log.info("init() end");
	}
	
	private void createViewsAndHandlers() throws Exception
	{
		log.info("createViewsAndHandlers start");		
		for(ViewConfig vi: viewInfoList)
		{
			ArrayList<ViewConfig> childList = viewChildren.get(vi.getId());
			
			if(vi.isNotLeafView())
			{
				if(childList == null)
					throw new IllegalStateException("view with id: "+vi.getId()+" is not parent of any other view. Check config !!!");				
			}

			log.info("creating a view for id: "+vi.getId());
			View view = createNewView();
			view.init(vi, childList, viewMap, viewHandler);
			viewMap.put(vi.getId(), view);
		}	
		log.info("createViewsAndHandlers end");		
	}
	
	private View createNewView() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		View view = (View) Class.forName(viewConfig.getViewImplClass()).newInstance();
		return view;
	}
	
	public void start()
	{		
		log.info("main view start()");
		View view = viewMap.get(topMostViewId);
		view.startInteraction();
		log.info("main view end()");		
	}
}
