package com.ajoy.service.codegen.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.DBColumn;
import com.ajoy.model.codegen.DBInfo;
import com.ajoy.model.codegen.DBTable;
import com.ajoy.model.codegen.DBTables;
import com.ajoy.model.codegen.Databases;
import com.ajoy.model.codegen.ResponseCode;
import com.ajoy.service.codegen.workflow.CallContext;

/**
 * 
 * @author kalyanc
 *
 */
public class DBTablesBO extends BaseBO
{
	private static Logger log = LogManager.getLogger(DBTablesBO.class);
	private DBTablesDAO tablesDao = new DBTablesDAO();

	
	private void setUpConnection(CallContext context)
	{
		DbDAO dbInfoDAO = new DbDAO();
		ResponseCode<Databases> code = dbInfoDAO.readFromStorage(context.getSessionInfo());

		Databases dbs = code.getObject();
		DBInfo selectedDb = null;

		for(DBInfo info : dbs.getDbInfoList())
		{
			if(info.isSelected())
				selectedDb = info;				
		}
		
		if(!DBConnectionHelper.getInstance().exists(selectedDb))
			DBConnectionHelper.getInstance().addDataSource(selectedDb);
		Connection conn = DBConnectionHelper.getInstance().getConnection(selectedDb.getName());
		String schemaName = selectedDb.getUrl().substring(selectedDb.getUrl().lastIndexOf('/')+1);
		selectedDb.setSchemaName(schemaName);
		context.setSelectedDB(selectedDb);
		context.setConn(conn);
		
		log.info("setupConnection() used Schema: "+schemaName);
	}
	
	
	public ResponseCode<DBTables> getDBTables(CallContext context)
	{
		log.info("getDBTables() start");
		ResponseCode<DBTables> aCode = new ResponseCode<>();

		try
		{
			setUpConnection(context);
			aCode = tablesDao.getTables(context);
			log.info("getDBTables() end "+aCode.isSuccess()+" errorCode: "+aCode.getErrorCode());
		}
		catch(SQLException sqe)
		{
			log.error("Error ", sqe);
		}
		finally
		{
			context.closeConnection();
			context.setSelectedDB(null);
		}
		return aCode;
	}

	public ResponseCode<DBTable> getDBTableDetails(DBTable table, CallContext context)
	{
		log.info("getDBTableDetails() start");
		ResponseCode<DBTable> code = new ResponseCode<>(); //dao.getTable(table);
		try
		{
			setUpConnection(context);
			code = tablesDao.getTableDetails(context, table);
			log.info("getDBTables() end "+code.isSuccess()+" errorCode: "+code.getErrorCode());
		}
		catch(SQLException sqe)
		{
			log.error("Error ", sqe);
		}
		finally
		{
			context.closeConnection();
			context.setSelectedDB(null);
		}

		
		
		log.info("getDBTableDetails() end ");
		return code;
	}


	//Will go away after implementation
	private static DBTables tables = new DBTables();

	static
	{
		DBTable table;
		tables.setTableList(new ArrayList<DBTable>());

		tables.getTableList().add((table = new DBTable())); 		
		table.setName("txn_main_tbl");
		table.setColumnList(new ArrayList<>(5));		
		table.getColumnList().add(new DBColumn("txn_id", DBColumn.LongDataType));
		table.getColumnList().add(new DBColumn("txn_status", DBColumn.IntDataType));

		tables.getTableList().add((table = new DBTable())); 		
		table.setName("cust_info_tbl");
		table.setColumnList(new ArrayList<>(5));		
		table.getColumnList().add(new DBColumn("cust_id", DBColumn.LongDataType));
		table.getColumnList().add(new DBColumn("cust_status", DBColumn.IntDataType));
		table.getColumnList().add(new DBColumn("cust_fname", DBColumn.StringDataType));
		table.getColumnList().add(new DBColumn("cust_lname", DBColumn.StringDataType));


		tables.getTableList().add((table = new DBTable())); 		
		table.setName("ext_acct_dtls_tbl");
		table.setColumnList(new ArrayList<>(5));		
		table.getColumnList().add(new DBColumn("acct_id", DBColumn.LongDataType));
		table.getColumnList().add(new DBColumn("acct_status", DBColumn.IntDataType));		
	}
}


