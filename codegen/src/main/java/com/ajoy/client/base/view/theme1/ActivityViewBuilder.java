/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajoy.client.base.view.theme1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.FXViewBuilder;
import com.ajoy.client.base.view.FXViewBuilderInfo;
import com.ajoy.client.base.view.UIManager;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author kalyanc
 */
public class ActivityViewBuilder extends BaseViewBuilder implements FXViewBuilder
{
	private static Logger log = LogManager.getLogger(ActivityViewBuilder.class);
	private BorderPane pane;
	private VBox leftMenu;
	
	public ActivityViewBuilder()
	{    	
	}

	@Override
	
	public Node createFXView(FXViewBuilderInfo fxViewBuilderInfo)
	{
		setFXViewBuilderConfig(fxViewBuilderInfo);
		pane = new BorderPane();	 		
		leftMenu = new VBox();
		
		FXViewBuilderInfo info = getFXViewBuilderInfo();			
		if(info.getChildViewConfigList() != null && info.getChildViewConfigList().size()>0)
		{									
			for(FXViewBuilderInfo childInfo:  info.getChildViewConfigList())
			{
				try
				{
					MenuHandler handler = new MenuHandler(this);
					Hyperlink link = new Hyperlink(childInfo.getName());
					link.setId(childInfo.getId());	
					link.setOnAction(handler);
					leftMenu.getChildren().add(link);
					
					if(childInfo.isSelected())
						link.fire();

				}
				catch(Exception exp)
				{
					log.error("Error in creation",  exp);
				}
			}			
		}	
		
		pane.setLeft(leftMenu);
		setFXView(pane);
		return pane;
	}

	public void displayView(String viewName)
	{
		log.info("Setting activity: "+viewName+" as centre");
		FXViewBuilder builder  = UIManager.getFXViewBuilder(viewName);
		
		if(builder!=null)	
		{
			Node node = builder.getFXView();
			builder.aboutToDisplayFXView();
			pane.setCenter(node);			
		}
		else			
			log.info("View is not defined yet for "+viewName);
				
	}
	

}
