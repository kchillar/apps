package com.ajoy.client.codegen.view.daocode;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RemoveDAOMethodViewBuilder extends BaseViewBuilder implements FXViewBuilder, EventHandler<ActionEvent>
{
	private static Logger log = LogManager.getLogger(RemoveDAOMethodViewBuilder.class);
	private static final String emptyLabel = "  ";

	private ComboBox<DAOInfo> daoNameList = new ComboBox<DAOInfo>();
	
	private ComboBox<DAOMethodInfo> daoMethodNameList = new ComboBox<DAOMethodInfo>();	
	private Button save;
	private Button remove;
	private HBox buttonBox = new HBox();


	public RemoveDAOMethodViewBuilder()
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

		Node parent = createContentForAddPane();
		setFXView(parent);
		return parent;
	}
	
	private Node createContentForAddPane()
	{
		GridPane gp1  = new GridPane();	      
		gp1.setPadding(new Insets(10, 10, 10, 10));
	
		int row = 0;

		Text daoName = new Text("Select DAO ");
		gp1.add(daoName, 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoNameList, 2, row);
		
		row = addEmpty(gp1, ++row);

		gp1.add(new Text("Select Method "), 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(daoMethodNameList, 2, row);
				
		row = addEmpty(gp1, ++row);
		
		gp1.add(new Text(emptyLabel), 0, row);
		gp1.add(new Text(emptyLabel), 1, row);
		gp1.add(buttonBox, 2, row);
		

			
		return gp1;
	}

	private void setupButtons()
	{
		remove = new Button("Remove");
		remove.setOnAction(this);
		
		save = new Button("Save");
		save.setOnAction(this);
		
		buttonBox.getChildren().add(save);
		buttonBox.getChildren().add(new Text(" "));
		buttonBox.getChildren().add(remove);
	}

	
	private void fetchDAOInfoList()
	{
		List<DAOInfo> list = UIDataModel.get().getDaoInfoList();

		if(list != null && list.size() > 0)		
			daoNameList.getItems().addAll(list);
		else
			log.warn("Got empty list");
	}


	@Override
	public void handle(ActionEvent event) 
	{
		Object source = event.getSource();

		if(source == save)
		{
						
			List<DAOMethodInfo> list = daoMethodNameList.getItems();

			if(list.size() > 0)
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
