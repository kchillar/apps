package com.ajoy.client.base.view.theme1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.FXViewBuilder;
import com.ajoy.client.base.view.FXViewBuilderInfo;

import javafx.scene.Node;

/**
 * 
 * @author kalyanc
 *
 */
public abstract class BaseViewBuilder implements FXViewBuilder
{	
	private static Logger log = LogManager.getLogger(BaseViewBuilder.class);
	private FXViewBuilderInfo FXViewBuilderInfo;
	private Node FXView;
	
	
	public BaseViewBuilder()
	{
	}
	
	public Node getFXView() 
	{
		return FXView;
	}

	public void setFXView(Node fXView) 
	{
		FXView = fXView;
	}

	public FXViewBuilderInfo getFXViewBuilderInfo() 
	{
		return FXViewBuilderInfo;
	}

	public void setFXViewBuilderConfig(FXViewBuilderInfo fXViewBuilderConfig) 
	{
		this.FXViewBuilderInfo = fXViewBuilderConfig;
	}

	@Override
	public void showInfoMsg(String msg) 
	{
		/*
		if(getParentViewBuilder()!=null)
			getParentViewBuilder().showInfoMsg(msg);
		else
			log.warn("getParentView() is null in showInfoMsg(): "+this.getClass().getName()+". Please check the logic.");
			*/
	}

	@Override
	public void showErrorMsg(String msg) 
	{
		//getParentViewBuilder().showErrorMsg(msg);		
	}
	
	@Override
	public void clearMsg()
	{
		//getParentViewBuilder().clearMsg();		
	}

	@Override
	public void aboutToDisplayFXView()
	{
		showInfoMsg("");		
	}

	
}
