package com.pmc.fw.db.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.db.DBConnectionHelper;
import com.pmc.fw.db.DBConnectionHelperConfig;
import com.pmc.fw.model.ResponseCode;

public class BasicDBConnectionHelper implements DBConnectionHelper
{
	private static Logger log = LoggerFactory.getLogger(BasicDBConnectionHelper.class);	
	private DBConnectionHelperConfig config;
	
	public ResponseCode init(DBConnectionHelperConfig helperConfig)
	{
		ResponseCode code = new ResponseCode();
		config = helperConfig;
		
		try
		{  			
			log.info("Will try to load class: "+helperConfig.getClassname().trim());
			Class<?> driverClass =  Class.forName(helperConfig.getClassname().trim());
			log.info("Loaded class successfully "+helperConfig.getClassname());
			code.setSuccess(true);	
			return code;
		}
		catch(Exception exp)
		{ 
			log.error("Error in DB Connection creation", exp);
		}  
		return code;
	}

	protected void testConnection(Connection conn) throws SQLException
	{
		Statement stmt=conn.createStatement();  
		ResultSet rs=stmt.executeQuery("select 1 from dual");  
		while(rs.next())
		{
			if(log.isDebugEnabled())
				log.debug("test conn: "+rs.getInt(1));
		}		
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		Connection conn=DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword()); 
		testConnection(conn);
		conn.setAutoCommit(false);
		return conn;
	}
	
}
