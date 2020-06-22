package com.pmc.utils.diffdb;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.xml.XMLHelper;

public class CommonTablesHelper extends BaseHelper
{
	public static Logger log = LoggerFactory.getLogger(CommonTablesHelper.class);
	
	public CommonTablesHelper()
	{		
	}
	
	public CommonTablesHelper(String dbOneName, String dbTwoName, File outDir)
	{
		super(dbOneName, dbTwoName, outDir);
	}
	
	public ResponseCode<List<DBTableInfo>> getCommonTables()
	{
		log.info("diffTableData() start");
		ResponseCode<List<DBTableInfo>> code = new ResponseCode<>();		
		List<DBTableInfo> dbOneTables = getTables(getDbOne());
		List<DBTableInfo> dbTwoTables = getTables(getDbTwo());		
		List<DBTableInfo> missingInOne = new ArrayList<>();
		List<DBTableInfo> missingInTwo = new ArrayList<>();
		List<DBTableInfo> inBoth = new ArrayList<>();
				
		try
		{
			Map<String, DBTableInfo> tablesInOne = new HashMap<>();
			Map<String, DBTableInfo> tablesInTwo = new HashMap<>();			
			dbOneTables = getTables(getDbOne());
			dbTwoTables = getTables(getDbTwo());
			
			if(dbOneTables != null)
			{
				for(DBTableInfo table: dbOneTables)
					tablesInOne.put(table.getName(), table);
			}

			if(dbTwoTables != null)
			{
				for(DBTableInfo table: dbTwoTables)
				{
					tablesInTwo.put(table.getName(), table);					
					if(tablesInOne.get(table.getName()) == null)
						missingInOne.add(table);
					else
						inBoth.add(table);
				}
			}

			if(dbOneTables != null)
			{
				for(DBTableInfo table: dbOneTables)
				{
					if(tablesInTwo.get(table.getName()) == null)
						missingInTwo.add(table);					
				}
			}
			
			System.out.println("Common Tables: ");
			for(DBTableInfo table: inBoth)			
				System.out.println(" |--> "+table.getName());			
			
			log.info("Completed successfully ");
			log.info("Common In Both:  "+inBoth.size());
			log.info("Missing In One:  "+missingInOne.size());
			log.info("Missing In Two:  "+missingInTwo.size());			
			generateFiles(missingInOne, missingInTwo, inBoth);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		
		code.setObject(inBoth);
		code.setSuccess(true);
		log.info("diffTableData() end");
		return code;		
	}

	private List<DBTableInfo> getTables(String dbName)
	{				
		Connection conn = null;
		try
		{
			DAO dao = new DAO();
			conn = super.getConnection(dbName);
			dao.setConnection(conn);			
			ResponseCode<List<String>> aCode1 = dao.getTableNames();			
			if(aCode1.isSuccess())
			{
				List<String> oneTableNames = aCode1.getObject();
				ResponseCode<List<DBTableInfo>> aCode2 = dao.getTableDetailsByNames(oneTableNames);
				return aCode2.getObject();
			}
			else
				return null;			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
			return null;
		}
		finally
		{
			super.close(conn);
		}
	}
	
	private void generateFiles(List<DBTableInfo> missingInOne, List<DBTableInfo> missingInTwo, List<DBTableInfo> inBoth)
	{
		writeFile(getDbOne(), "tables-missing-in-"+getDbOne()+".xml", missingInOne);
		writeFile(getDbTwo(), "tables-missing-in-"+getDbTwo()+".xml", missingInTwo);
		writeFile(null, "tables-in-both-"+getDbOne()+"-"+getDbTwo()+".xml", inBoth);
	}
	
	private void writeFile(String dbName, String fileName, List<DBTableInfo> tableList)
	{
		try
		{
			File file = new File(getOutDir(), fileName);			
			DBInfo dbInfo = new DBInfo();
			dbInfo.setDbName(dbName);
			dbInfo.setDbTables(tableList);			
			XMLHelper.writeObjectToFile(DBInfo.class, dbInfo, file);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
	}	
}
