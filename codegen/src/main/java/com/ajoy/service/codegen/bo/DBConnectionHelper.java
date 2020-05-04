package com.ajoy.service.codegen.bo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.DBInfo;

public class DBConnectionHelper 
{
	private static Logger log = LogManager.getLogger(DBConnectionHelper.class);
	
	private Map<String, DataSource> dataSourceMap = new HashMap<>();
	private Map<String, DBInfo> infoMap = new HashMap<>();
	private static DBConnectionHelper singleton = new DBConnectionHelper();
	
	
	private DBConnectionHelper()
	{		
	}
	
	public static DBConnectionHelper getInstance()
	{
		return singleton;
	}
	
	public boolean exists(DBInfo dbInfo)
	{
		if(infoMap.get(dbInfo.getName()) != null)
			return true;
		else
			return false;
	}
	
	public void addDBInfo(DBInfo dbInfo)
	{
		
		if(infoMap.get(dbInfo.getName()) == null)
		{
			singleton.addDataSource(dbInfo);
		}
	}
	
	public void addDataSource(DBInfo dbInfo)
	{
		log.info("addingDataSource("+dbInfo.getName()+") start");
		
		BasicDataSource aDataSource = new BasicDataSource();
		aDataSource.setUrl(dbInfo.getUrl());
		aDataSource.setUsername(dbInfo.getUser());
		aDataSource.setPassword(dbInfo.getPass());
		aDataSource.setDriverClassName(dbInfo.getDriverClassName());
		
		// Min number of connections to be created in the pool
		aDataSource.setInitialSize(1);
		
		// Man number of connections that be created in the pool
		aDataSource.setMaxActive(10);
		// The eviction thread will check this often for unused connection to bring down the pool
		// size
		aDataSource.setTimeBetweenEvictionRunsMillis(60000);
			// If a connection is idle (not used) for more than this millis it can be clean by the
	        	// cleaning thread
	    aDataSource.setMinEvictableIdleTimeMillis(300000);

	        // The maximum number of millis the getConnection() on data source will wait
	    aDataSource.setMaxWait(10000);
	    
	    DataSource source = (DataSource) aDataSource;
	    
	    dataSourceMap.put(dbInfo.getName(), source);
	    
	    infoMap.put(dbInfo.getName(), dbInfo);
	    
	    log.info("addingDataSource("+dbInfo.getName()+") end");	    
	}
	
	public Connection getConnection(String dbName)
	{
		try
		{
			log.info("getConnection() start");
			DataSource source = dataSourceMap.get(dbName);
			
			if(source == null)
				throw new IllegalStateException("Unable to find data source with name: "+dbName);
			
			Connection conn = source.getConnection();
			log.info("getConnection() end");
			return conn;
		}
		catch(Exception exp)
		{
			throw new IllegalStateException("Unable to get connection to db: "+dbName, exp);
		}
	}
	

}
