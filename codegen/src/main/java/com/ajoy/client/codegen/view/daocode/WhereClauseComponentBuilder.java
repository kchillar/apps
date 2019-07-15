package com.ajoy.client.codegen.view.daocode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.theme1.BaseViewBuilder;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 
 * @author kalyanc
 *
 */
public class WhereClauseComponentBuilder extends BaseViewBuilder 
{
	private static Logger log = LogManager.getLogger(WhereClauseComponentBuilder.class);

	private List<String> tableColumnList;
	private List<String> objectFieldList;
	
	private KeyCode prevKeyCode ;
	private TextArea textArea = new TextArea();

	public WhereClauseComponentBuilder(List<String> dbTableColumnList, List<String> javaObjectFieldList)
	{
		tableColumnList = dbTableColumnList;
		objectFieldList = javaObjectFieldList;		
	}
	

	@Override
	public Node createFXView(com.ajoy.client.base.view.FXViewBuilderInfo fxViewBuilderInfo) 
	{
		textArea.setText("WHERE  ");		
		textArea.setOnKeyPressed(keyPressedEventHandler);
		return textArea;
	}

	@Override
	public void displayView(String viewPath) 
	{
		// TODO Auto-generated method stub		
	}


	private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() 
	{
		public void handle(final KeyEvent keyEvent) 
		{
			KeyCode keyCode = keyEvent.getCode();

			//System.out.println("pressed key "+keyCode.getName()+" "+keyCode);

			if(keyCode.CONTROL == keyCode)
			{        		 
				log.info("Got COntrol");
				prevKeyCode = keyCode.CONTROL;        		 
			}
			else if(keyCode.O == keyCode)
			{
				log.info("Got space");
				if(prevKeyCode == keyCode.CONTROL)
				{
					System.out.println("Show object dialog");
					Object source = keyEvent.getSource();


					ChoiceDialog<String> dialog = createObjectDialog(null);        			 
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent())
					{
						textArea.appendText(result.get());
						System.out.println("Your choice: " + result.get());
					}        			 
				}
				prevKeyCode = keyCode;
			}
			else if(keyCode.T == keyCode)
			{
				//log.info("Got space");
				if(prevKeyCode == keyCode.CONTROL)
				{
					System.out.println("Show Table dialog");
					Object source = keyEvent.getSource();

					ChoiceDialog<String> dialog = createTableDialog(null);        			 
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent())
					{
						textArea.appendText(result.get());
						System.out.println("Your choice: " + result.get());
					}        			 
				}

				prevKeyCode = keyCode;
			}

			else
			{
				prevKeyCode = keyCode;
			}
		}
	};


	private ChoiceDialog<String> createObjectDialog(String key)
	{				
		return createDialog(objectFieldList);
	}

	private ChoiceDialog<String> createTableDialog(String key)
	{				
		return createDialog(tableColumnList);
	}

	private ChoiceDialog<String> createDialog(List<String> choices)
	{
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Database Table Colum Selector");
		dialog.setHeaderText("");
		dialog.setContentText("Column ");		
		return dialog;
	}
}
