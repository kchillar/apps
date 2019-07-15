/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajoy.client.base.view.theme1;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.UIManager;
import com.ajoy.client.base.view.FXViewBuilder;
import com.ajoy.client.base.view.FXViewBuilderInfo;
import com.ajoy.client.codegen.main.UIDataModel;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


/**
 *
 * @author kalyanc
 */
public class MainViewBuilder extends BaseViewBuilder implements FXViewBuilder
{	
	private static Logger log = LogManager.getLogger(MainViewBuilder.class);    
    private BorderPane pane;
    private VBox top = new VBox();
    private HBox topMenu;
    private Label msgDisplayArea = new Label();
            
    public MainViewBuilder()
    {    	    	
    }


    
	@Override
	public Node createFXView(FXViewBuilderInfo fxViewBuilderInfo)
	{
		pane = new BorderPane(); 			
		setFXViewBuilderConfig(fxViewBuilderInfo);
		
		setupMenus();
		top.getChildren().add(topMenu);		
		top.getChildren().add(getNewOffsetLabel());
				
		HBox box = new HBox();
		box.getChildren().add(getNewOffsetLabel());
		box.getChildren().add(getNewOffsetLabel());
		box.getChildren().add(msgDisplayArea);		
		
		top.getChildren().add(box);
		msgDisplayArea.setText("application messages will appear here");
		pane.setTop(top);
		
		Pane rightPane = new Pane();
		rightPane.getChildren().add(getNewOffsetLabel());
		pane.setRight(rightPane);
		
		VBox vbox = new VBox();
		vbox.getChildren().add(getNewOffsetLabel());
		pane.setBottom(vbox);
		
		setFXView(pane);
		return pane;
	}

	private void setupMenus()
	{
		topMenu = new HBox();
		topMenu.getChildren().add(getNewOffsetLabel());
		
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
					topMenu.getChildren().add(link);
					
					if(childInfo.isSelected())
						link.fire();
				}
				catch(Exception exp)
				{
					log.error("Error in creation",  exp);
				}
			}			
		}	
	}

	private Label getNewOffsetLabel()
	{
		return new Label("                    ");
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


	@Override
	public void showInfoMsg(String msg) 
	{
		msgDisplayArea.setText(msg);
	}

	@Override
	public void showErrorMsg(String msg) 
	{
		msgDisplayArea.setText("ERROR: "+msg);
	}
	
	@Override
	public void clearMsg()
	{
		msgDisplayArea.setText("");
	}

}
