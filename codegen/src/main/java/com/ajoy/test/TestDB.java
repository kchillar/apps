package com.ajoy.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.DBColumn;
import com.ajoy.model.codegen.DBInfo;
import com.ajoy.model.codegen.DBTable;
import com.ajoy.model.codegen.DBTables;
import com.ajoy.model.codegen.Databases;
import com.ajoy.model.codegen.ResponseCode;
import com.ajoy.model.codegen.SessionInfo;
import com.ajoy.service.codegen.bo.DBConnectionHelper;
import com.ajoy.service.codegen.bo.DBTablesBO;
import com.ajoy.service.codegen.bo.DbDAO;
import com.ajoy.service.codegen.workflow.CallContext;

public class TestDB 
{
	private static Logger log = LogManager.getLogger(TestDB.class);
	
	public static void main(String[] args) throws SQLException
	{
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setSelectedProfile("Dev");

		CallContext context  = new CallContext();
		context.setSessionInfo(sessionInfo);
		
		DBTablesBO bo = new DBTablesBO();
		ResponseCode<DBTables> code = bo.getDBTables(context);
		
		if(code.isSuccess())
		{
			for(DBTable table: code.getObject().getTableList())
			{
				log.info("Got table: "+table.getName());								
				ResponseCode<DBTable> aCode = bo.getDBTableDetails(table, context);
				
				for(DBColumn col: aCode.getObject().getColumnList())
				{
					log.info(" Got Column: "+col.getName());
				}
			}
		}
		
	}
	
	public static void main1(String[] args) throws SQLException
	{
		log.info("TestDB start");
		
		SessionInfo sessioninfo = new SessionInfo();
		sessioninfo.setSelectedProfile("Dev");
		
		
		DbDAO dao = new DbDAO();
		ResponseCode<Databases> code = dao.readFromStorage(sessioninfo);
		
		Databases dbs = code.getObject();
		DBInfo selectedDb = null;
		
		for(DBInfo info : dbs.getDbInfoList())
		{
			if(info.isSelected())
				selectedDb = info;				
		}
		
		if(!DBConnectionHelper.getInstance().exists(selectedDb))
			DBConnectionHelper.getInstance().addDataSource(selectedDb);
		
		String sql = null;
		sql = "select * from app_tbl";
		sql = "select table_name from information_schema.tables where table_schema='cryptodb_dev'";		
		
		
		Connection conn = DBConnectionHelper.getInstance().getConnection(selectedDb.getName());
		PreparedStatement pr = conn.prepareStatement(sql);		
		ResultSet rs = pr.executeQuery();		
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next())
		{
			for(int i=0; i< rsmd.getColumnCount(); i++)
				System.out.print("Col["+i+"] = "+rs.getString(i+1)+", ");			
			System.out.println();
		}
		
		log.info("GOT CONNECTION "+conn);
		
		log.info("TestDB end");
	}
}
