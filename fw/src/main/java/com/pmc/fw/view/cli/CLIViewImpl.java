package com.pmc.fw.view.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.Param;
import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.model.ViewEventImpl;
import com.pmc.fw.view.View;
import com.pmc.fw.view.ViewConfig;
import com.pmc.fw.view.ViewEvent;
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
		setReader(new BufferedReader(new InputStreamReader(System.in)));
	}
	
	public String presentMenuAndGetSelection() 
	{
		try 
		{
			System.out.println("----------------------------------------------------");			
			String option = "";
			while ("".equals(option)) 
			{				
				for (int i = 0; i < getChildList().size(); i++)
					System.out.println(
							i + " -- " + getChildList().get(i).getLabel() + " ( event-id:"+getChildList().get(i).getEventId()+", view-id: " + getChildList().get(i).getId() +" )");
				System.out.println("Q -- To exit ");
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
					ViewConfig vc = getChildList().get(i);
					View ic = getViewMap().get(vc.getId());
					log.info("option: " + option + " got vi.id: " + vc.getId());
					if (vc.isNotLeafView()) 
					{						
						ic.startInteraction();
						break;
					} 
					else 
					{	
						Map<String, Object> map = null;

						if(vc.getInputParams() != null)
						{
							map = new HashMap<String, Object>();
							for(Param p: vc.getInputParams())							
								readParamIntoMap(p, map);															
						}	
						
						ViewEvent event = new ViewEventImpl(vc.getId(), vc.getEventId(), this, map);						
						ResponseCode code = null;
						if(ic.getEventHandler() == null)
							code = getEventHandler().handleEvent(event);							
						else
							code = ic.getEventHandler().handleEvent(event);
							
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
