package com.ajoy.test;

import java.util.Arrays;
import java.util.List;

import com.ajoy.client.base.view.FXViewBuilderInfo;
import com.ajoy.client.codegen.main.AppUI;
import com.ajoy.client.codegen.view.daocode.FromDBColumnsToJavaFieldsMapperComponent;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application 
{    	
	//private static Logger log = LogManager.getLogger(Test.class);
	private FXViewBuilderInfo config;
	private static Log log = new Log(Test.class);
	 
	public Test()
	{
		log.info("Created "+this);		
	}

	public void init()
	{
		log.info("init() start");
		
		Parameters params = getParameters();
		
		if(params != null)
		{
			try
			{
				//String viewConfigFile = params.getRaw().get(0);
				//log.debug("Got viewConfigFile: "+viewConfigFile);				
			}
			catch(Exception exp)
			{
				log.error("Unable to get confguration to create UI", exp);
			}
		}
		else
		{
			log.debug("No Params");
		}

		log.info("init() end");
	}
	
    @Override
    public void start(Stage stage) throws Exception 
    {
    	log.info("start() start");
    	//TabelViewBuilder builder = new TabelViewBuilder();
    	//TextListBuilder builder = new TextListBuilder();
    	FromDBColumnsToJavaFieldsMapperComponent builder = new FromDBColumnsToJavaFieldsMapperComponent(getTableList(), getObjectList());
        Parent root = (Parent) builder.createComponent();      
        Scene scene = new Scene(root, 500, 500);    
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
        log.info("start() end");
    }
    
    
	private static final List<String> getTableList()
	{
		return Arrays.asList("t1.c1", "t1.c2", "t2.c1","t2.c2");
	}


	private static final List<String> getObjectList()
	{
		return Arrays.asList("in1.f1.f12", "in1.f1.f11", "in1.f1","in2.f1");
	}


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {    	
    	AppUI.launch(args);
    }
 

}
