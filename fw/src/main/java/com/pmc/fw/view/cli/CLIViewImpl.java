package com.pmc.fw.view.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.Param;
import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.model.ViewEventImpl;
import com.pmc.fw.view.View;
import com.pmc.fw.view.ViewConfig;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;
import com.pmc.fw.view.ViewEventHandlerImpl;
import com.pmc.fw.view.ViewImpl;


/**
 * 
 * @author kalyanc
 *
 */
public class CLIViewImpl extends ViewImpl 
{
	public static Logger log = LoggerFactory.getLogger(ViewImpl.class);

	private BufferedReader reader;

	public CLIViewImpl() 
	{
	}

	public ResponseCode init(ViewConfig vInfo, List<ViewConfig> childList, Map<String, View> viewMap, ViewEventHandler handler) 
	{		
		ResponseCode code = super.init(vInfo, childList, viewMap, handler);		
		setReader(new BufferedReader(new InputStreamReader(System.in)));
		return code;
	}

	public String presentMenuAndGetSelection() 
	{
		try 
		{
			String option = "";
			while ("".equals(option)) 
			{
				System.out.println("Q -- To exit ");
				for (int i = 0; i < getChildList().size(); i++)
					System.out.println(
							i + " -- " + getChildList().get(i).getLabel() + "(" + getChildList().get(i).getId() + ")");
				System.out.print("Please enter an option: ");
				option = getReader().readLine();
				option = option.trim();

				if (option.length() < 1)
					System.out.print("Invalid option:" + option + " try again !!!");
				else
					return option;
			}
			return option;
		} 
		catch (Exception exp) 
		{
			log.error("Error", exp);
			return null;
		}
	}

	public void startInteraction() 
	{
		log.info("start()");

		while (true) 
		{
			String option = presentMenuAndGetSelection();
			if (option.equalsIgnoreCase("q"))
				break;

			for (int i = 0; i < getChildList().size(); i++) 
			{
				if (option.equals(i + "")) 
				{
					ViewConfig vi = getChildList().get(i);
					log.info("option: " + option + " got vi.id: " + vi.getId());
					if (vi.isNotLeafView()) 
					{
						View ic = getViewMap().get(vi.getId());
						ic.startInteraction();
						break;
					} 
					else 
					{	
						Map<String, Object> map = null;

						if(vi.getInputParams() != null)
						{
							map = new HashMap<String, Object>();
							for(Param p: vi.getInputParams())							
								readParamIntoMap(p, map);															
						}	
						
						ViewEvent event = new ViewEventImpl(vi.getId(),this, map);						
						ResponseCode code = getViewHandler().handleEvent(event);
						break;
					}
				}
			}
		}
		log.info("end()");
	}

	public BufferedReader getReader() {
		return this.reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	private void readParamIntoMap(Param p, Map<String, Object>map)
	{
		while(true)
		{
			String val = "";
			try
			{
				System.out.print("Enter value for "+p.getName()+" (default="+p.getValue()+"): ");
				 val = reader.readLine();
				
				if(val.trim().length()==0)
				{
					val = p.getValue();
					System.out.println("using default value of: "+val);					
				}
				
				if(p.getType() == null || "s".equalsIgnoreCase(p.getType()))
				{
					map.put(p.getName(), val);
					break;
				}
				else if("i".equalsIgnoreCase(p.getType()))
				{
					map.put(p.getName(), Integer.parseInt(val));
					break;
				}
				else if("d".equalsIgnoreCase(p.getType()))
				{
					map.put(p.getName(), Double.parseDouble(val));
					break;
				}
			}
			catch(Exception exp)
			{
				System.err.println("Invalid value "+val);
			}
		}
	}
}
