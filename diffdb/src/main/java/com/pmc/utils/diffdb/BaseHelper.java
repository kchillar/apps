package com.pmc.utils.diffdb;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.db.DBConnectionManager;
import com.pmc.fw.resources.ResourceProvider;

public class BaseHelper 
{
	public static Logger log = LoggerFactory.getLogger(BaseHelper.class);
	private String dbOne;
	private String dbTwo;
	private File outDir;
	
	public BaseHelper()
	{		
	}
	
	public BaseHelper(String dbOneName, String dbTwoName, File outDir)
	{
		setDBNames(dbOneName, dbTwoName);
		setOutDir(outDir);
	}
		
	public void setDBNames(String dbOneName, String dbTwoName)
	{
		dbOne = dbOneName;
		dbTwo = dbTwoName;
	}
	
	public File getOutDir() {
		return outDir;
	}

	public void setOutDir(File outDir) {
		this.outDir = outDir;
	}

	public String getDbOne() {
		return dbOne;
	}

	public void setDbOne(String dbOne) {
		this.dbOne = dbOne;
	}

	public String getDbTwo() {
		return dbTwo;
	}

	public void setDbTwo(String dbTwo) {
		this.dbTwo = dbTwo;
	}

	protected Connection getConnection(String name) throws SQLException
	{
		DBConnectionManager manager = (DBConnectionManager) ResourceProvider.getResource(DBConnectionManager.class.getSimpleName()); 
		Connection conn = manager.getConnection(name);
		return conn;
	}
	
	protected void close(Connection conn)
	{
		try
		{
			if(conn!=null)
				conn.close();
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
	}
}
