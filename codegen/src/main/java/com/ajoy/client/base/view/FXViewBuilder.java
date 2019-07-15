package com.ajoy.client.base.view;

import javafx.scene.Node;

/**
 * 
 * @author kalyanc
 *
 */
public interface FXViewBuilder 
{
	public static final String PathSeperator = "/";
	
	public FXViewBuilderInfo getFXViewBuilderInfo();		
	public Node createFXView(FXViewBuilderInfo fxViewBuilderInfo); 
	public Node getFXView();
	    			
	public void aboutToDisplayFXView();
	public void displayView(String viewPath);
	
	public void showInfoMsg(String msg);
	public void showErrorMsg(String msg);
	public void clearMsg();
	
}
