package com.pmc.utils.diffdb;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;

public class TableDataDiffHelper extends BaseHelper
{
	public static Logger log = LoggerFactory.getLogger(TableDataDiffHelper.class);
	
	public TableDataDiffHelper()
	{		
	}
	
	public TableDataDiffHelper(String dbOneName, String dbTwoName, File outDir)
	{
		super(dbOneName, dbTwoName, outDir);
	}
	
	public ResponseCode diffTableData(DBTableInfo tableInfo)
	{
		ResponseCode code = new ResponseCode<>();
		ResponseCode<List<String>> code1 = diffTableData(tableInfo, getDbOne(), getDbTwo());
		
		if(code1.isSuccess())		
			generateData(tableInfo, "data-only-in-"+getDbTwo()+"-"+tableInfo.getName()+".txt", code1.getObject());
				
		ResponseCode<List<String>> code2 = diffTableData(tableInfo, getDbTwo(), getDbOne());
		if(code2.isSuccess())		
			generateData(tableInfo, "data-only-in-"+getDbOne()+"-"+tableInfo.getName()+".txt", code2.getObject());
		
		return code;
	}
	
	
	public ResponseCode<List<String>> diffTableData(DBTableInfo tableInfo, String fromDB, String toDB)
	{
		log.info("diffTableData() start");
		ResponseCode<List<String>> code = new ResponseCode();
		
		try
		{
			Map<String, Object> dataMap = getTableData(tableInfo, fromDB);			
			DAO dao = new DAO();
			Connection conn = super.getConnection(toDB);
			dao.setConnection(conn);			
			code = dao.getMissingData(tableInfo, dataMap);
			code.setSuccess(true);
			log.info("Completed successfully "+code.getObject());
		}
		catch(Exception exp)
		{			
			log.error("Error", exp);
		}
		
		code.setSuccess(true);
		log.info("diffTableData() end");
		return code;
		
	}
	
	private Map<String, Object> getTableData(DBTableInfo tableInfo, String dbName)
	{				
		Connection conn = null;
		try
		{
			DAO dao = new DAO();
			conn = super.getConnection(dbName);
			dao.setConnection(conn);			
			ResponseCode<Map<String, Object>> code = dao.getTableData(tableInfo);
			return code.getObject();
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

	private void generateData(DBTableInfo tableInfo, String fileName, List<String> dataList)
	{
		try
		{
			File file = new File(getOutDir(), fileName);
			PrintWriter pr = new PrintWriter(file);
			
			for(int i=0; i<tableInfo.getColumns().size();i++)
			{
				pr.print(tableInfo.getColumns().get(i).getName());
				if(i < tableInfo.getColumns().size() - 1)
					pr.print("-");
			}
			pr.println();
			
			if(dataList != null && dataList.size() > 0)
			{
				for(String line: dataList)
					pr.println(line);
				pr.flush();			
			}
			pr.close();
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
	}

}
