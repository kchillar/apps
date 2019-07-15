package com.ajoy.client.codegen.view.daocode;



import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.test.AutoCompleteTextField;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * 
 * @author kalyanc
 *
 */
public class FromDBColumnsToJavaFieldsMapperComponent extends DBColumnsJavaFieldsMapperComponent 
{
	private static Logger log = LogManager.getLogger(FromDBColumnsToJavaFieldsMapperComponent.class);
	
	

	public FromDBColumnsToJavaFieldsMapperComponent()
	{
	}

	
	public FromDBColumnsToJavaFieldsMapperComponent(List<String> dbTableColumnList, List<String> javaObjectFieldList)
	{
		setTableColumnList(dbTableColumnList);
		setObjectFieldList(javaObjectFieldList);		
	}
	
	protected void setupLabels()
	{
		HBox box1 = new HBox();
		box1.getChildren().add(new Text(EmptySpace));
		box1.getChildren().add(new Text("   Java Object Field"));

		HBox box2 = new HBox();
		box2.getChildren().add(new Text(EmptySpace));
		box2.getChildren().add(new Text("    DB Column/Expression "));		
		box2.getChildren().add(new Text(EmptySpace));
		box2.getChildren().add(new Text("   Select"));
		
		columnBox1.getChildren().add(box1);
		columnBox2.getChildren().add(box2);		
	}
	
	protected Node createColumn1Field()
	{
		AutoCompleteTextField javaField = new AutoCompleteTextField();
		((AutoCompleteTextField )javaField).getEntries().addAll(getObjectFieldList());
		return javaField;
	}

	protected Node createColumn2Field()
	{
		TextField dbField = new TextField();			
		dbField.setOnKeyPressed(getKeyPressedEventHandler());
		return dbField;
	}

}