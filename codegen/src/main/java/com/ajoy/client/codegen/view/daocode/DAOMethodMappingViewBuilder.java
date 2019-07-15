package com.ajoy.client.codegen.view.daocode;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.FXViewBuilder;
import com.ajoy.client.base.view.FXViewBuilderInfo;
import com.ajoy.client.base.view.theme1.BaseViewBuilder;
import com.ajoy.client.codegen.main.UIDataModel;
import com.ajoy.model.codegen.DAOInfo;
import com.ajoy.model.codegen.DAOMethodInfo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author kalyanc
 *
 */

public class DAOMethodMappingViewBuilder extends BaseViewBuilder implements FXViewBuilder, EventHandler<ActionEvent>
{
	private static Logger log = LogManager.getLogger(DAOMethodMappingViewBuilder.class);
	private static final String emptyLabel = " ";

	private ComboBox<DAOInfo> daoNameList = new ComboBox<DAOInfo>();
	private ComboBox<DAOMethodInfo> daoMethodNameList = new ComboBox<DAOMethodInfo>();	
	private Button cancel;
	private Button save;
	private HBox buttonBox = new HBox();
	
	private ScrollPane scrollPane = new ScrollPane();
	private Node columnsToJavaMappingPanel;		
	private Node javaToColumnsMappingPanel;
	
	private TitledPane wherePane;
	private Node whereClausePanel;
	


	public DAOMethodMappingViewBuilder()
	{		
	}

	private int addEmptyRow(GridPane gp, int row)
	{
		gp.add(new Text(emptyLabel), 0, row);
		gp.add(new Text(emptyLabel), 1, row);
		gp.add(new Text(emptyLabel), 2, row++);
		return row;
	}

	@Override
	public Node createFXView(FXViewBuilderInfo fxViewBuilderInfo)
	{
		daoMethodNameList.setOnAction(this);
		setupButtons();
		fetchDAOInfoList();
		setupMethodNameList();

		GridPane gPane = createContentForAddPane();
				
		Accordion accordian = new Accordion();								
		TitledPane editPane = createMappingsPane();
		TitledPane wherePane = createWherePane();

	    accordian.getPanes().addAll(editPane, wherePane);
		
		VBox vbox = new VBox();
		vbox.getChildren().add(gPane);
		vbox.getChildren().add(accordian);	
		vbox.getChildren().add(new Text(emptyLabel));
		vbox.getChildren().add(buttonBox);
		setFXView(vbox);
		return vbox;
	}

	private TitledPane createMappingsPane()
	{				
		scrollPane .setPrefHeight(600);
	    scrollPane .prefWidth(500);
		
		// Create Second TitledPane.
		TitledPane pane = new TitledPane();
		pane.setText("Mappings");
		FromDBColumnsToJavaFieldsMapperComponent componentCreator1 = new FromDBColumnsToJavaFieldsMapperComponent(getTableList(), getObjectList());
		columnsToJavaMappingPanel = componentCreator1.createComponent();
		
		DBColumnsJavaFieldsMapperComponent componentCreator2 = new FromJavaFieldsToDBColumnsMapperComponent(getTableList(), getObjectList());
		javaToColumnsMappingPanel = componentCreator2.createComponent();
		
		scrollPane .setContent(javaToColumnsMappingPanel);
		pane.setContent(scrollPane );
		return pane;
	}
	
	private TitledPane createWherePane()
	{
		// Create Second TitledPane.
		wherePane = new TitledPane();
		wherePane.setText("Where Condition");
		WhereClauseComponentBuilder componentCreator = new WhereClauseComponentBuilder(getTableList(), getObjectList());
		whereClausePanel = componentCreator.createFXView(null);
		wherePane.setContent(whereClausePanel);
		wherePane.setDisable(true);
		return wherePane;
	}

	private static final List<String> getTableList()
	{
		return Arrays.asList("t1.c1", "t1.c2", "t2.c1","t2.c2");
	}


	private static final List<String> getObjectList()
	{
		return Arrays.asList("in1.f1.f12", "in1.f1.f11", "in1.f1","in2.f1");
	}
	
	private void fetchDAOInfoList()
	{
		List<DAOInfo> list = UIDataModel.get().getDaoInfoList();

		if(list != null && list.size() > 0)		
			daoNameList.getItems().addAll(list);
		else
			log.warn("Got empty list");
	}

	
	private void setupMethodNameList()
	{
		DAOMethodInfo info;
		
		info = new DAOMethodInfo("getCustomerTxns");
		info.setSqlType(DAOMethodInfo.SelectSqlType);
		daoMethodNameList.getItems().add(info);

		info = new DAOMethodInfo("getCustomers");
		info.setSqlType(DAOMethodInfo.SelectSqlType);
		daoMethodNameList.getItems().add(info);
		
		info = new DAOMethodInfo("createCustomer");
		info.setSqlType(DAOMethodInfo.InsertSqlType);
		daoMethodNameList.getItems().add(info);		

		info = new DAOMethodInfo("updateCustomer");
		info.setSqlType(DAOMethodInfo.UpdateSqlType);
		daoMethodNameList.getItems().add(info);		
		
	}
	
	private GridPane createContentForAddPane()
	{
		GridPane gp1  = new GridPane();	      
		gp1.setPadding(new Insets(10, 10, 10, 10));
	
		int row = 0;

		Text daoName = new Text("Select DAO ");
		gp1.add(daoName, 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoNameList, 2, row++);
		row = addEmptyRow(gp1, row);
		
		Text methodName = new Text("Select Method");
		gp1.add(methodName, 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoMethodNameList, 2, row++);		
		row = addEmptyRow(gp1, row);
	
		return gp1;
	}

	private void setupButtons()
	{
		cancel = new Button("Cancel");
		cancel.setOnAction(this);		
		save = new Button("Save");
		save.setOnAction(this);
		buttonBox.getChildren().add(cancel);
		buttonBox.getChildren().add(save);
	}

	@Override
	public void handle(ActionEvent event) 
	{
		Object source = event.getSource();

		if(source == save)
		{
		}
		else if(source == cancel)
		{
			DAOMethodInfo dao = daoMethodNameList.getSelectionModel().getSelectedItem();
			daoMethodNameList.getItems().remove(dao);
			log.info("Removed "+dao);
		}
		else if(source == save)
		{
			List<DAOMethodInfo> list = daoMethodNameList.getItems();

			if(list.size() > 0)
			{
				DAOInfo daoInfo = new DAOInfo();
				daoInfo.setMethodList(list);
				//UIDataModel.get().updateDaoInfo(daoInfo);
			}
		}
		else if(source == daoMethodNameList)
		{
			DAOMethodInfo dao = daoMethodNameList.getSelectionModel().getSelectedItem();
			log.info("Selected "+dao.getName()+" type: "+dao.getSqlType());
			
			if(dao.getSqlType().equals(DAOMethodInfo.InsertSqlType) || dao.getSqlType().equals(DAOMethodInfo.UpdateSqlType))
			{
				if(dao.getSqlType().equals(DAOMethodInfo.InsertSqlType))
					wherePane.setDisable(true);
				else
					wherePane.setDisable(false);
				
				scrollPane.setContent(javaToColumnsMappingPanel); //pass list data to the view and call reset before display 
			}
			else
			{
				wherePane.setDisable(false);
				scrollPane.setContent(columnsToJavaMappingPanel); //pass list data to the view and call reset before display
			}
		}
	}

	@Override
	public void displayView(String viewName) 
	{
	}

}
