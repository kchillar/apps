package com.pmc.utils.diffdb;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.xml.XMLHelper;

public class TableStructureDiffHelper extends BaseHelper
{
	public static Logger log = LoggerFactory.getLogger(TableStructureDiffHelper.class);
	private DBTableInfo missingInDBOne = new DBTableInfo();
	private DBTableInfo missingInDBTwo = new DBTableInfo();

	public TableStructureDiffHelper()
	{		
	}
	
	public TableStructureDiffHelper(String dbOneName, String dbTwoName, File outDir)
	{
		super(dbOneName, dbTwoName, outDir);
	}
	
	public ResponseCode<?> diffTableStructure(DBTableInfo tableInfo)
	{
		ResponseCode<?> code = new ResponseCode<>();		
		ResponseCode<DBTableInfo> code1 = getTableDataInfo(tableInfo, getDbOne());
		ResponseCode<DBTableInfo> code2 = getTableDataInfo(tableInfo, getDbTwo());

		Map<String, DBColumnInfo> columMapOne = new HashMap<>();
		Map<String, DBColumnInfo> columnMapTwo = new HashMap<>();
		
		if(code1.isSuccess() && code2.isSuccess())
		{
			if(code1.getObject()!= null && code1.getObject().getColumns() != null)
			{
				for(DBColumnInfo colInfo: code1.getObject().getColumns())				
					columMapOne.put(colInfo.getName(), colInfo);				
			}
			
			if(code2.getObject()!= null && code2.getObject().getColumns() != null)
			{
				for(DBColumnInfo colInfo: code2.getObject().getColumns())
				{
					columnMapTwo.put(colInfo.getName(), colInfo);
					DBColumnInfo colOfOne = columMapOne.get(colInfo.getName());					
					if(colOfOne != null)
					{
					  if(!(colInfo.getDataType().equals(colOfOne.getDataType()) &&  (colInfo.getColumnSize() == colOfOne.getColumnSize())))						
							missingInDBOne.addColumnInfo(colOfOne);						
					}
					else
						missingInDBOne.addColumnInfo(colOfOne);
				}
			}

			if(code1.getObject()!= null && code1.getObject().getColumns() != null)
			{
				for(DBColumnInfo colInfo: code1.getObject().getColumns())	
				{
					DBColumnInfo colOfTwo = columnMapTwo.get(colInfo.getName());					
					if(colOfTwo != null)
					{
						if(!(colInfo.getDataType().equals(colOfTwo.getDataType()) &&  (colInfo.getColumnSize() == colOfTwo.getColumnSize())))							
							missingInDBTwo.addColumnInfo(colOfTwo);						
					}					
					else					
						missingInDBTwo.addColumnInfo(colOfTwo);									
				}									
			}
			
			code.setSuccess(true);			
			generateFiles(tableInfo);
		}
		
		return code;
	}
	
	private void generateFiles(DBTableInfo tableInfo)
	{
		if(missingInDBOne.getColumns() != null && missingInDBOne.getColumns().size() > 0)
		{
			File file = new File(getOutDir(), "missing-in-"+getDbOne()+"-"+tableInfo.getName()+".xml");
			XMLHelper.writeObjectToFile(DBTableInfo.class, missingInDBOne, file);			
		}		
		if(missingInDBTwo.getColumns() != null && missingInDBTwo.getColumns().size() > 0)
		{
			File file = new File(getOutDir(), "missing-in-"+getDbTwo()+"-"+tableInfo.getName()+".xml");
			XMLHelper.writeObjectToFile(DBTableInfo.class, missingInDBTwo, file);			
		}
	}
	
	public ResponseCode<DBTableInfo> getTableDataInfo(DBTableInfo tableInfo, String dbName)
	{
		ResponseCode<DBTableInfo> code = new ResponseCode<>();		
		try
		{				
			DAO dao = new DAO();
			Connection conn = super.getConnection(dbName);
			dao.setConnection(conn);						
			code = dao.getDBTableInfo(tableInfo.getName());
		}
		catch(Exception exp)
		{			
			log.error("Error", exp);
			code = new ResponseCode<>();
		}
		return code;		
	}


}
