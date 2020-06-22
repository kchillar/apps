package com.pmc.utils.diffdb;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;


public class DBDiffEventHandler implements ViewEventHandler 
{
	public static Logger log = LoggerFactory.getLogger(DBDiffEventHandler.class);
	
	private static final String ShowDBNames = "Show_DB_Names";
	private static final String SetDBNames = "Set_DB_Names";
	private static final String ComputeCommonTableList = "Compute_Common_Tables";
	private static final String ShowCommonTableList = "Show_Common_Tables";
	private static final String DiffTableData = "Diff_DB_Table_Data";
	private static final String DiffTableStructure = "Diff_DB_Table_Structure";
	
	private File outDir = new File("./output");
	private String dbOneName = "DB1";
	private String dbTwoName = "DB2";
	private List<DBTableInfo> commonTableList;
	
	public DBDiffEventHandler() 
	{
		try
		{
			outDir.mkdirs();
		}
		catch(Exception exp)
		{
			log.error("Error in creation of output data dir "+outDir.getPath(), exp);
		}
	}
	
	@Override
	public ResponseCode<?> handleEvent(ViewEvent event) 
	{
		ResponseCode<?> code = new ResponseCode<>();
		String eventId = event.getEventId();		
		log.info("Event: "+eventId+" params: "+event.getEventData());
		switch(eventId)
		{
		case ShowDBNames:
			System.out.println("First Database Name: "+dbOneName+", Second Database Name: "+dbTwoName);
			break;
		case ComputeCommonTableList:
			handleComputeTablesEvent(event);
			break;						
		case ShowCommonTableList:
			handleShowTables(event);
			break;
		case DiffTableData:
			handleDiffTableData(event);
			break;
		case DiffTableStructure:
			handleDiffTableStructure(event);
			break;			
		case SetDBNames:
			handleSetDBNames(event);
			break;
		default:
			log.info("default");
		}
		log.info("completed event handling");
		System.out.println("\n\n");		
		return code;
	}
	
	private void handleSetDBNames(ViewEvent event)
	{
		dbOneName = (String) event.getEventData().get("FirstDatabaseName");
		dbTwoName = (String) event.getEventData().get("SecondDatabaseName");
		System.out.println("Updated firstDatabase name to : "+dbOneName+" and secondDatabase name to: "+dbTwoName);
	}
	
	private void handleDiffTableStructure(ViewEvent event)
	{
		log.info("Diff DB Table Structure");				
		Integer index = (Integer) event.getEventData().get("TableIndex");			
		if(commonTableList != null && commonTableList.size() > 0)
		{
			if(index < 0 ||  index > commonTableList.size()-1)
			{
				System.out.println(" ?? ******** Invalid Idex: "+index+" valid range [0,"+(commonTableList.size()-1)+"]");					
			}
			else
			{
				DBTableInfo tableInfo = commonTableList.get(index);
				System.out.println("Will process table: "+tableInfo.getName());
				TableStructureDiffHelper tHelper = new TableStructureDiffHelper(dbOneName, dbTwoName, outDir);
				tHelper.diffTableStructure(tableInfo);
			}
		}
		else
		{
			System.out.println("Could not find common tables. Try running option 'Compute Common Tables'");
		}
	}

	private void handleDiffTableData(ViewEvent event)
	{
		log.info("Diff DB Tables");				
		Integer index = (Integer) event.getEventData().get("TableIndex");			
		if(commonTableList != null && commonTableList.size() > 0)
		{
			if(index < 0 ||  index > commonTableList.size()-1)
			{
				System.out.println(" ?? ******** Invalid Idex: "+index+" valid range [0,"+(commonTableList.size()-1)+"]");					
			}
			else
			{
				DBTableInfo tableInfo = commonTableList.get(index);
				System.out.println("Will process table: "+tableInfo.getName());
				TableDataDiffHelper tHelper = new TableDataDiffHelper(dbOneName, dbTwoName, outDir);
				tHelper.diffTableData(tableInfo);
			}
		}
		else
		{
			System.out.println("Could not find common tables. Try running option 'Compute Common Tables'");
		}
	}
	
	private void handleShowTables(ViewEvent event)
	{
		if(commonTableList != null && commonTableList.size() > 0)
		{
			System.out.println("Common Tables Are: ");
			for(int i=0; i<commonTableList.size(); i++)			
				System.out.println(" --"+i+" -> "+commonTableList.get(i).getName());
		}
		else
		{
			System.out.println("Could not find common tables. Try running option 'Compute Common Tables'");
		}
	}
	
	private void handleComputeTablesEvent(ViewEvent event)
	{
		System.out.println("Compute Common tables ");
		commonTableList = null;
		CommonTablesHelper cHelper = new CommonTablesHelper(dbOneName, dbTwoName, outDir);
		ResponseCode<List<DBTableInfo>> aCode = cHelper.getCommonTables();			
		if(aCode.isSuccess())
			commonTableList = aCode.getObject();
		else
			System.out.println("Could not compute the common tables list");			
	}
}
