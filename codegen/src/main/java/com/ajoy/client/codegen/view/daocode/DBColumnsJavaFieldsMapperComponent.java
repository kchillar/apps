package com.ajoy.client.codegen.view.daocode;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author kalyanc
 *
 */
public abstract class DBColumnsJavaFieldsMapperComponent implements EventHandler<ActionEvent> 
{
	private static Logger log = LogManager.getLogger(DBColumnsJavaFieldsMapperComponent.class);
	protected static final String EmptySpace = "  ";
	protected static final String EqualSign = " =  ";
	
	private KeyCode prevKeyCode ;
		
	protected GridPane gridPane = new GridPane();			
	protected VBox columnBox1 = new VBox();	
	protected VBox columnBox2 = new VBox();
	
	protected HBox buttonBox = new HBox();
	protected Button clear;
	protected Button add;
	
	
	private List<String> tableColumnList;
	private List<String> objectFieldList;
	
	protected abstract void setupLabels();
	protected abstract Node createColumn1Field();	
	protected abstract Node createColumn2Field();
		
	public Node createComponent()
	{
		initGridPane();
		setupLabels();			
		addEmptyRow();		
		addRow();		
		setupButtons();	
		addButtonsRow();		
		return gridPane;
	}
	
	protected void initGridPane()
	{		      
		gridPane.setMinSize(200, 200); 
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		//Setting the vertical and horizontal gaps between the columns 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       
		gridPane.setAlignment(Pos.TOP_LEFT); 			
		gridPane.add(columnBox1, 0, 0);	
		gridPane.add(columnBox2, 1, 0);		
	}
		
	private void setupButtons()
	{
		clear = new Button("Remove");
		add = new Button("Add");
		add.setOnAction(this);
		clear.setOnAction(this);	
		
		buttonBox.getChildren().add(new Text(EmptySpace));
		buttonBox.getChildren().add(new Text(EmptySpace));
		buttonBox.getChildren().add(clear);
		buttonBox.getChildren().add(new Text(EmptySpace));
		buttonBox.getChildren().add(add);
		buttonBox.getChildren().add(new Text(EmptySpace));
	}

	private void addButtonsRow()
	{	
		columnBox1.getChildren().add(new Text(EmptySpace));
		columnBox2.getChildren().add(buttonBox);				
	}

	private void addEmptyRow()
	{				
		columnBox1.getChildren().add(new Text(EmptySpace));		
		columnBox2.getChildren().add(new Text(EmptySpace));
	}
	
	private void addRow()
	{				
		Node colum1Field = createColumn1Field();
		Node colum2Field = createColumn2Field();		
		CheckBox cb = new CheckBox();
				
		HBox box1 = new HBox();
		box1.getChildren().add(new Text(EmptySpace));
		box1.getChildren().add(colum1Field);

		HBox box2 = new HBox();
		box2.getChildren().add(new Text(EqualSign));
		box2.getChildren().add(colum2Field);
		box2.getChildren().add(new Text(EmptySpace));
		box2.getChildren().add(cb);
		
		columnBox1.getChildren().add(box1);
		columnBox2.getChildren().add(box2);	
		addEmptyRow();
	}

	private void moveButtonsRowAndAddRow()
	{		
		int index = columnBox1.getChildren().size()-1;		
		columnBox1.getChildren().remove(index);		
		columnBox2.getChildren().remove(index);		
		addRow();
		addButtonsRow();		
	}

			
	@Override
	public void handle(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(source == add)
		{
			moveButtonsRowAndAddRow();
		}
		else if(source == clear)
		{
			List<Node> col1 = new ArrayList<>();
			List<Node> col2 = new ArrayList<>();
			
			
			for(int i=0 ; i < columnBox2.getChildren().size() - 1; i++)				
			{		
				Node node = columnBox2.getChildren().get(i);				
				if(node instanceof HBox && i>0)
				{
					HBox box = (HBox) node;				
					CheckBox cb = (CheckBox)box.getChildren().get(3); //We know it is 3 as we codede this class
				
					if(cb.isSelected())
					{
						log.info(i+" is selected");	
						col1.add ( columnBox1.getChildren().get(i));
						col1.add ( columnBox1.getChildren().get(i+1));
						col2.add ( columnBox2.getChildren().get(i));
						col2.add ( columnBox2.getChildren().get(i+1));
					}								
				}									
				i++;
			}
			columnBox1.getChildren().removeAll(col1);
			columnBox2.getChildren().removeAll(col2);
		}		
	}
	
	protected List<String> getTableColumnList() {
		return tableColumnList;
	}
	protected void setTableColumnList(List<String> tableColumnList) {
		this.tableColumnList = tableColumnList;
	}
	protected List<String> getObjectFieldList() {
		return objectFieldList;
	}
	protected void setObjectFieldList(List<String> objectFieldList) {
		this.objectFieldList = objectFieldList;
	}
	
	protected EventHandler<KeyEvent> getKeyPressedEventHandler() 
	{
		return keyPressedEventHandler;
	}


	protected EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() 
	{
		public void handle(final KeyEvent keyEvent) 
		{
			KeyCode keyCode = keyEvent.getCode();
			if(keyCode.CONTROL == keyCode)
			{        		 
				log.info("Got COntrol");
				prevKeyCode = keyCode.CONTROL;        		 
			}
			else if(keyCode.T == keyCode)
			{
				//log.info("Got space");
				if(prevKeyCode == keyCode.CONTROL)
				{
					System.out.println("Show Table dialog");
					Object source = keyEvent.getSource();
					TextField field = new TextField();

					ChoiceDialog<String> dialog = createTableDialog(null);        			 
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent())
					{
						field.appendText(result.get());
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
		return createDialog(getObjectFieldList());
	}

	private ChoiceDialog<String> createTableDialog(String key)
	{				
		return createDialog(getTableColumnList());
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
