package com.ajoy.service.codegen.bo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.DBColumn;
import com.ajoy.model.codegen.DBInfo;
import com.ajoy.model.codegen.DBTable;
import com.ajoy.model.codegen.DBTables;
import com.ajoy.model.codegen.ResponseCode;
import com.ajoy.service.codegen.workflow.CallContext;

public class DBTablesDAO extends BaseDAO
{
	private static Logger log = LogManager.getLogger(DBTablesDAO.class);
	
	public DBTablesDAO()
	{		
	}
	
	public ResponseCode<DBTables> getTables(CallContext context) throws SQLException
	{
		try
		{
			log.info("getTables() start");			
			ResponseCode<DBTables> code = new ResponseCode<DBTables>();			
			DBInfo dbInfo = context.getSelectedDB();
			String sql = "select table_name from information_schema.tables where table_schema=?";				
			PreparedStatement pr = context.getConn().prepareStatement(sql);		
			
			pr.setString(1,dbInfo.getSchemaName());			
			ResultSet rs = pr.executeQuery();		
			ResultSetMetaData rsmd = rs.getMetaData();
			
			DBTables tables = new DBTables();
			code.setObject(tables);
			
			while(rs.next())
			{
				for(int i=0; i< rsmd.getColumnCount(); i++)
					log.info("Col["+i+"] = "+rs.getString(i+1)+" ");
				
				if(tables.getTableList() == null)
					tables.setTableList(new ArrayList<>());
				
				DBTable table = new DBTable();
				table.setName(rs.getString(1));
				
				tables.getTableList().add(table);								
			}
			
			code.setSuccess(true);
			log.info("getTables() end");
			return code;
		}
		finally
		{			
		}
	}
	
	public ResponseCode<DBTable> getTableDetails(CallContext context, DBTable table) throws SQLException
	{
		try
		{
			log.info("getTableDetails() start");			
			ResponseCode<DBTable> code = new ResponseCode<DBTable>();			
			String sql = "select column_name from information_schema.columns where table_name=?";			
			PreparedStatement pr = context.getConn().prepareStatement(sql);					
			pr.setString(1,table.getName());			
			ResultSet rs = pr.executeQuery();		
			code.setObject(table);
			
			while(rs.next())
			{				
				if(table.getColumnList() == null)
					table.setColumnList(new ArrayList<>());				
				DBColumn col = new DBColumn();
				col.setName(rs.getString(1));				
				table.getColumnList().add(col);								
			}
			
			code.setSuccess(true);
			log.info("getTableDetails() end");
			return code;
		}
		finally
		{			
		}
	}
}
