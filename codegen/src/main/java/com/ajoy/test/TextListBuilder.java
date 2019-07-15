package com.ajoy.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author kalyanc
 *
 */
public class TextListBuilder implements EventHandler<ActionEvent> 
{
	private static Log log = new Log(TextListBuilder.class);
	
	private VBox vbox = new VBox();
	private HBox labelBox;
	private HBox buttonBox;
	private Button clear;
	private Button add;
	private Button save;
	private List<CheckBox> checkBoxList = new ArrayList<>();

	
	public Parent build()
	{
		setupLabelBox();
		setupButtons();
		vbox.getChildren().add(labelBox);
		vbox.getChildren().add(buttonBox);
		addATextInputRow();
		return vbox;
	}
	
	private void setupLabelBox()
	{
		labelBox = new HBox();
		/*
		labelBox.getChildren().add( new Text ("[DB Column Or Expression]"));
		labelBox.getChildren().add( new Text (" = "));
		labelBox.getChildren().add( new Text ("[Java Object Field] "));
		*/
		
		labelBox.getChildren().add( new Text ("(Get Data from DB column or expression]"));
		labelBox.getChildren().add( new Text (" = "));
		labelBox.getChildren().add( new Text ("(Set data on Object field) "));

	}
	
	private void setupButtons()
	{
		buttonBox = new HBox();
		clear = new Button("Remove");
		add = new Button("Add");
		//save = new Button("Save");
		//buttonBox.getChildren().add(save);
		buttonBox.getChildren().add(new Text("  "));
		buttonBox.getChildren().add(clear);
		buttonBox.getChildren().add(new Text("  "));
		buttonBox.getChildren().add(add);
		add.setOnAction(this);
		clear.setOnAction(this);
	}
	
	private HBox createTextRowBox()
	{
		TextField dbField = new TextField();
		dbField.setOnKeyReleased(keyReleasedEventHandler);		
		CheckBox checkBox = new CheckBox();

		TextField javaObject = new AutoCompleteTextField();
		((AutoCompleteTextField)javaObject).getEntries().addAll(getObjectList());		
		
		Text equalSymbol = new Text(" = ");
		Text space = new Text("  ");
		
		HBox box = new HBox();					
		box.getChildren().add(dbField);
		box.getChildren().add(equalSymbol);
		box.getChildren().add(javaObject);
		
		box.getChildren().add(space);
		box.getChildren().add(checkBox);				
		checkBoxList.add(checkBox);		

		return box;
	}
	
	private void addATextInputRow()
	{
		int index = vbox.getChildren().size()-1;			
		vbox.getChildren().remove(index);
		HBox box = createTextRowBox();
		vbox.getChildren().add(box);
		vbox.getChildren().add(buttonBox);

	}

	private static final List<String> getTableList()
	{
		return Arrays.asList("t1.c1", "t1.c2", "t2.c1","t2.c2");
	}

	
	private static final List<String> getObjectList()
	{
		return Arrays.asList("in1.f1.f12", "in1.f1.f11", "in1.f1","in2.f1");
	}
	
	@Override
	public void handle(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(source == add)
		{
			addATextInputRow();
		}
		else if(source == clear)
		{
			List<CheckBox> list = new ArrayList<>();
			List<Node> boxList = new ArrayList<>();
			
			int i=0;
			for(CheckBox checkBox : checkBoxList)				
			{								
				if(checkBox.isSelected())
				{
					log.info(i+" is selected");	
					list.add(checkBoxList.get(i));
					boxList.add(vbox.getChildren().get(i+1)); //first is label box, so add 1					
				}
				
				i++;
			}			
			checkBoxList.removeAll(list);
			vbox.getChildren().removeAll(boxList);
		}		
	}

	EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() 
    {
        public void handle(final KeyEvent keyEvent) 
        {
        	 KeyCode keyCode = keyEvent.getCode();
        	 
        	 if(keyCode.PERIOD == keyCode)
        	 {
        		 log.info("Released key "+keyCode.getName()+" "+keyCode);
        		 
        		 Object source = keyEvent.getSource();
        		 
        		 if(source instanceof TextField)
        		 {
        			 TextField txField = (TextField)source; 
        			 String key = txField.getText();   
        			 
        			 log.info("Got text: "+key);
        			 
        			 ChoiceDialog<String> dialog = createDialog(key);        			 
        			 Optional<String> result = dialog.showAndWait();
        			 if (result.isPresent())
        			 {
        			     System.out.println("Your choice: " + result.get());
        			 }        			 
        		 }
        	 }
        }
    };
		
	private ChoiceDialog<String> createDialog(String key)
	{				
		return createDialog(getTableList());
	}

	private ChoiceDialog<String> createDialog(List<String> choices)
	{
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");		
		return dialog;
	}

	
}
