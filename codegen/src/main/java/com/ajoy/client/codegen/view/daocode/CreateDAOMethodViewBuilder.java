package com.ajoy.client.codegen.view.daocode;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.client.base.view.FXViewBuilder;
import com.ajoy.client.base.view.FXViewBuilderInfo;
import com.ajoy.client.base.view.theme1.BaseViewBuilder;
import com.ajoy.client.codegen.main.UIDataModel;
import com.ajoy.model.codegen.DAOInfo;
import com.ajoy.model.codegen.DAOMethodInfo;
import com.ajoy.model.codegen.DBTable;
import com.ajoy.model.codegen.FieldInfo;
import com.jfoenix.controls.JFXChipView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * @author kalyanc
 *
 */
public class CreateDAOMethodViewBuilder extends BaseViewBuilder implements FXViewBuilder, EventHandler<ActionEvent>
{
	private static Logger log = LogManager.getLogger(CreateDAOMethodViewBuilder.class);
	private static final String emptyLabel = "        ";

	private ComboBox<DAOInfo> daoNameList = new ComboBox<DAOInfo>();
	private TextField daoMethodNameField = new TextField();
	
	private ComboBox<String> sqlTypeList = new ComboBox<>();	
	private CheckBox singleMulti = new CheckBox();


	private JFXChipView<DBTable> dbTableChip = new JFXChipView<>();
	private JFXChipView<FieldInfo> inputObjectChip = new JFXChipView<>();
	private JFXChipView<FieldInfo> outputObjectChip = new JFXChipView<>();

	private Button add;
	private Button cancel;
	private Button save;
	private Button remove;
	private HBox buttonBox = new HBox();


	public CreateDAOMethodViewBuilder()
	{		
	}

	private int addEmpty(GridPane gp, int row)
	{
		gp.add(new Text(emptyLabel), 0, row);
		gp.add(new Text(emptyLabel), 1, row);
		gp.add(new Text(emptyLabel), 2, row++);
		return row;
	}

	@Override
	public Node createFXView(FXViewBuilderInfo fxViewBuilderInfo)
	{
		setupButtons();
		fetchDAOInfoList();
		setupSqlTypeList();
		setupDBTableList();
		setupInputObjectList();
		setupOutputObjectList();

		Node parent = createContentForAddPane();
		setFXView(parent);
		return parent;
	}
	
	private Node createContentForAddPane()
	{
		GridPane gp1  = new GridPane();	      
		gp1.setPadding(new Insets(10, 10, 10, 10));
	
		int row = 0;

		Text methodName = new Text("Create Method Name");
		gp1.add(methodName, 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoMethodNameField, 2, row);


		row = addEmpty(gp1, ++row);
		
		Text daoName = new Text("In DAO");
		gp1.add(daoName, 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoNameList, 2, row);


		row = addEmpty(gp1, ++row);
		
		gp1.add(new Text("SQL Type"), 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(sqlTypeList, 2, row);
		

		row = addEmpty(gp1, ++row);
		
		gp1.add(new Text("Multi Row"), 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(singleMulti, 2, row);
				
		row = addEmpty(gp1, ++row);
		
		gp1.add(new Text("Use "), 0, row);
		gp1.add(new Text(emptyLabel), 1, row);		
		gp1.add(new Text(emptyLabel), 2, row);
		
		TabPane tabpane = new TabPane();
		Tab tab = new Tab("Tables");
		tab.setContent(dbTableChip);
		tabpane.getTabs().add(tab);
		
		tab = new Tab("Input Objects");
		tab.setContent(inputObjectChip);
		tabpane.getTabs().add(tab);
		
		tab = new Tab("Output Object");
		tab.setContent(outputObjectChip);
		tabpane.getTabs().add(tab);
		
		
		VBox vbox = new VBox();
		vbox.getChildren().add(gp1);
		vbox.getChildren().add(tabpane);
		vbox.getChildren().add(buttonBox);
				
		return vbox;
	}

	private void setupButtons()
	{
		remove = new Button("Remove");
		remove.setOnAction(this);
		
		cancel = new Button("Cancel");
		cancel.setOnAction(this);		
		save = new Button("Save");
		save.setOnAction(this);

		add = new Button("Add");
		add.setOnAction(this);

		
		buttonBox.getChildren().add(cancel);
		buttonBox.getChildren().add(save);
		buttonBox.getChildren().add(add);
	}

	private void setupDBTableList()
	{
		List<DBTable> list = new ArrayList<>(3);
		DBTable table;
		table = new DBTable();
		table.setName("txn_main_tbl");
		list.add(table);

		table = new DBTable();
		table.setName("cust_info_tbl");
		list.add(table);

		table = new DBTable();
		table.setName("cust_profile_tbl");
		list.add(table);

		dbTableChip.getSuggestions().addAll(list);
		
		//dbTableList.getItems().addAll(list);
	}

	
	private void fetchDAOInfoList()
	{
		List<DAOInfo> list = UIDataModel.get().getDaoInfoList();

		if(list != null && list.size() > 0)		
			daoNameList.getItems().addAll(list);
		else
			log.warn("Got empty list");
	}

	private void setupSqlTypeList()
	{
		List<String> list = new ArrayList<String>(4);
		list.add("Insert");
		list.add("Select");
		list.add("Update");
		//list.add("Prodcedure");
		sqlTypeList.getItems().addAll(list);
	}


	private void setupInputObjectList()
	{
		List<FieldInfo> list = new ArrayList<>(3);
		FieldInfo info;
		info = new FieldInfo();
		info.setClassname("com.alacriti.model.DataVO");
		list.add(info);

		info = new FieldInfo();
		info.setClassname("com.alacriti.model.Bank");
		list.add(info);

				
		inputObjectChip.getSuggestions().addAll(list);
	}

	private void setupOutputObjectList()
	{
		List<FieldInfo> list = new ArrayList<>(3);
		FieldInfo info;
		info = new FieldInfo();
		info.setClassname("com.alacriti.model.DataVO");
		list.add(info);

		info = new FieldInfo();
		info.setClassname("com.alacriti.model.Bank");
		list.add(info);

		outputObjectChip.getSuggestions().addAll(list);
	}


	@Override
	public void handle(ActionEvent event) 
	{
		Object source = event.getSource();

		if(source == add)
		{
			if(daoMethodNameField.getText().trim().length() > 0)
			{				
				DAOMethodInfo info = new DAOMethodInfo(daoMethodNameField.getText().trim());
				info.setSql( sqlTypeList.getSelectionModel().getSelectedItem());

				log.info("Added");
			}
		}
		else if(source == cancel)
		{
		}
		else if(source == save)
		{
			
			List<DBTable> tList = dbTableChip.getChips();
			
			
			if(tList != null && tList.size()>0)
			{
				for(DBTable table: tList)
					log.info("Selected table: "+table);				
			}
			else
			{
				log.info("No table seelcted");
			}
			
			
			List<DAOMethodInfo> list = null;

			if(list != null && list.size() > 0)
			{
				DAOInfo daoInfo = new DAOInfo();
				daoInfo.setMethodList(list);
				//UIDataModel.get().updateDaoInfo(daoInfo);
			}
		}
	}

	@Override
	public void displayView(String viewName) 
	{
	}

}
