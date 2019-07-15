package com.ajoy.client.codegen.view.daocode;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.test.AutoCompleteTextField;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class FromJavaFieldsToDBColumnsMapperComponent extends DBColumnsJavaFieldsMapperComponent 
{
	private static Logger log = LogManager.getLogger(FromJavaFieldsToDBColumnsMapperComponent.class);
		
	public  FromJavaFieldsToDBColumnsMapperComponent()
	{		
	}
	
	public FromJavaFieldsToDBColumnsMapperComponent(List<String> dbTableColumnList, List<String> javaObjectFieldList)
	{
		setTableColumnList(dbTableColumnList);
		setObjectFieldList(javaObjectFieldList);		
	}

	
	protected void setupLabels()
	{
		HBox box1 = new HBox();
		box1.getChildren().add(new Text(EmptySpace));
		box1.getChildren().add(new Text("    DB Column/Expression "));		
		

		HBox box2 = new HBox();
		box2.getChildren().add(new Text(EmptySpace));
		box2.getChildren().add(new Text("    Java Object Field"));
		box2.getChildren().add(new Text(EmptySpace));
		box2.getChildren().add(new Text("              Select"));
		
		columnBox1.getChildren().add(box1);
		columnBox2.getChildren().add(box2);		
	}
	
	protected Node createColumn1Field()
	{
		TextField dbField = new TextField();
		dbField.setOnKeyReleased(getKeyPressedEventHandler());	
		return dbField;
	}

	protected Node createColumn2Field()
	{
		AutoCompleteTextField javaField = new AutoCompleteTextField();
		((AutoCompleteTextField )javaField).getEntries().addAll(getObjectFieldList());
		return javaField;
	}
	

}