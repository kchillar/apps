package com.pmc.utils.diffdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;

public class DAO 
{
	public static Logger log = LoggerFactory.getLogger(TableDataDiffHelper.class);	
	private Connection connection;

	public ResponseCode<List<String>> getTableNames()
	{
		PreparedStatement pr = null;
		ResultSet rs = null;
		ResponseCode<List<String>> code = new ResponseCode<>();
		List<String> list = new ArrayList<>();
		String sql = "select table_name from information_schema.tables where table_type='BASE TABLE'"; 
		try
		{
			pr = getConnection().prepareStatement(sql);			
			rs = pr.executeQuery();			
			while(rs.next())
				list.add(rs.getString(1));				
			if(list.size() > 1)
			{
				code.setObject(list);
				code.setSuccess(true);
			}
			else
				code.setCode("No tables");
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
		finally
		{
			close(pr, rs);
		}
		return code;
	}

	private List<DBTableInfo> createDBTableInfoList(List<String> tableNames)
	{
		if(tableNames == null || tableNames.size() < 1)
			return null;
		
		List<DBTableInfo> list = new ArrayList<>(tableNames.size());
		
		for(String tableName: tableNames)				
			list.add((new DBTableInfo(tableName)));
						
		return list;		
	}
	
	public ResponseCode<List<DBTableInfo>> getTableDetailsByNames(List<String> tableNames)
	{
		ResponseCode<List<DBTableInfo>> code = new ResponseCode<>();
		List<DBTableInfo> list = createDBTableInfoList(tableNames);
		
		if(list != null)
		{
			code = this.getTableDetails(list);
		}
		else
		{
			code.setCode("Invalid List");
		}		
		return code;
	}
	
	public ResponseCode<List<DBTableInfo>> getTableDetails(List<DBTableInfo> tableNames)
	{
		PreparedStatement pr = null;
		ResultSet rs = null;
		ResponseCode<List<DBTableInfo>> code = new ResponseCode<>();
		String sql = "select column_name, data_type from information_schema.columns where table_name = ?";
		
		try
		{
			pr = getConnection().prepareStatement(sql);			
						
			for(DBTableInfo table: tableNames)
			{
				pr.setString(1, table.getName());			
				rs = pr.executeQuery();
				while(rs.next())
				{
					DBColumnInfo columnInfo = new DBColumnInfo();
					columnInfo.setName(rs.getString(1));
					columnInfo.setDataType(rs.getString(2));
					table.addColumnInfo(columnInfo);
				}				
				close(null, rs);
				log.info("completed getting columns for table: "+table.getName());
			}
			
			code.setObject(tableNames);
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
		finally
		{
			close(pr, null);
		}
		return code;
	}
		
	public ResponseCode<DBTableInfo> getDBTableInfo(String tableName)
	{
		PreparedStatement pr = null;
		ResultSet rs = null;
		ResponseCode<DBTableInfo> code = new ResponseCode<>();
		String sql = "select column_name, data_type from information_schema.columns where table_name = ?";
		
		try
		{			
			DBTableInfo dbTable = new DBTableInfo();
			dbTable.setName(tableName);
			pr = getConnection().prepareStatement(sql);									
			pr.setString(1, tableName);			
			rs = pr.executeQuery();
			while(rs.next())
			{
				DBColumnInfo columnInfo = new DBColumnInfo();
				columnInfo.setName(rs.getString(1));
				columnInfo.setDataType(rs.getString(2));
				dbTable.addColumnInfo(columnInfo);
			}				
			close(null, rs);
			log.info("completed getting columns for table: "+tableName);
			code.setObject(dbTable);
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
		finally
		{
			close(pr, null);
		}
		return code;
	}

	public ResponseCode<Map<String, Object>> getTableData(DBTableInfo tableInfo)
	{
		int size = 0;
		
		if(tableInfo.getColumns() != null)
			size = tableInfo.getColumns().size();
		
		tableInfo.getColumns().size();
		PreparedStatement pr = null;
		ResultSet rs = null;
		ResponseCode<Map<String, Object>> code = new ResponseCode<>();
		
		StringBuilder buff = new StringBuilder();
		buff.append("select ");
		for(int i=0; i< size; i++)
		{			
			buff.append(tableInfo.getColumns().get(i).getName());
			if(i< size - 1)
				buff.append(", ");
		}
		
		buff.append(" from "+tableInfo.getName());
		
		String sql = buff.toString();
		
		if(log.isDebugEnabled())
			log.debug("Select SQL: "+sql);
		
		Object object = new Object();
		Map<String, Object> rows = new HashMap<>();
		
		try
		{
			pr = getConnection().prepareStatement(sql);									
			rs = pr.executeQuery();
			
			while(rs.next())
			{
				StringBuilder rowBuff = new StringBuilder();
				for(int i=0; i<size; i++)
					rowBuff.append(rs.getString(i+1)+"-");
				rows.put(rowBuff.toString(), object);
				//System.out.println("Row: "+rowBuff.toString());
			}				
			log.info("completed getting data for table: "+tableInfo.getName()+" rows: "+rows.size());
			code.setObject(rows);
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
		finally
		{
			close(pr, rs);
		}
		return code;
	}

	public ResponseCode<List<String>> getMissingData(DBTableInfo tableInfo, Map<String, Object> dataMap)
	{
		int size = 0;
		
		if(tableInfo.getColumns() != null)
			size = tableInfo.getColumns().size();
		
		tableInfo.getColumns().size();
		PreparedStatement pr = null;
		ResultSet rs = null;
		ResponseCode<List<String>> code = new ResponseCode<>();
		
		StringBuilder buff = new StringBuilder();
		buff.append("select ");
		for(int i=0; i< size; i++)
		{			
			buff.append(tableInfo.getColumns().get(i).getName());
			if(i< size - 1)
				buff.append(", ");
		}
		
		buff.append(" from "+tableInfo.getName());		
		String sql = buff.toString();		
		if(log.isDebugEnabled())
			log.debug("Select SQL: "+sql);		
		Object object = new Object();
		List<String> missingRows = new ArrayList<>();
		
		try
		{
			pr = getConnection().prepareStatement(sql);									
			rs = pr.executeQuery();
			
			while(rs.next())
			{
				StringBuilder rowBuff = new StringBuilder();
				for(int i=0; i<size; i++)
					rowBuff.append(rs.getString(i+1)+"-");
				String row = rowBuff.toString();
				
				if(dataMap.get(row) == null)
					missingRows.add(row);								
			}				

			log.info("completed getting data for table: "+tableInfo.getName()+" rows: "+missingRows.size());
			code.setObject(missingRows);
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
		finally
		{
			close(pr, rs);
		}
		return code;
	}


	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private void close(PreparedStatement pr, ResultSet rs)
	{
		try
		{
			if(pr != null)
				pr.close();
		}
		catch(Exception exp)
		{
			
		}
		
		try
		{
			if(rs != null)
				rs.close();
		}
		catch(Exception exp)
		{
			
		}
	}
}
