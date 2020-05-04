package com.ajoy.service.codegen.workflow;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.DBInfo;
import com.ajoy.model.codegen.SessionInfo;

public class CallContext 
{
	private static Logger log = LogManager.getLogger(CallContext.class);
	
	private SessionInfo sessionInfo;
	private DBInfo selectedDB;
	
	private Connection conn;
	
	public void setSessionInfo(SessionInfo sessionInfo)
	{
		this.sessionInfo = sessionInfo;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public DBInfo getSelectedDB() {
		return selectedDB;
	}

	public void setSelectedDB(DBInfo selectedDB) {
		this.selectedDB = selectedDB;
	}
	
	public void closeConnection()
	{
		if(conn != null)
		{
			try
			{
				conn.close();				
			}
			catch(Exception exp)
			{
				log.warn("Error in closing connnection");
			}
		}
		
		conn = null;
	}
	
}
