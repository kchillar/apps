package com.pmc.fw.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.xml.XMLHelper;

public class DBConnectionManagerImpl implements DBConnectionManager
{
	private static Logger log = LoggerFactory.getLogger(DBConnectionManagerImpl.class);
	
	private Map< String, DBConnectionHelper> dbConnectionHelperMap = new HashMap<>();
	
	@Override
	public ResponseCode init(InputStream configStream) 
	{
		ResponseCode code = new ResponseCode();
		log.info("init() start");
		try
		{
			DBConnectionManagerConfig config = (DBConnectionManagerConfig) XMLHelper.getObjectFromInputStream(DBConnectionManagerConfig.class, configStream);
			
			if(config.getConfigList() == null || config.getConfigList().isEmpty())
			return code;
			
			Class<?> dbHelperClass = Class.forName(config.getDbConnectionHelperClassname());
			
			for(DBConnectionHelperConfig helperConfig: config.getConfigList())
			{
				DBConnectionHelper helper = (DBConnectionHelper) dbHelperClass.newInstance();
				ResponseCode aCode = helper.init(helperConfig);				
				log.info("DBConnectionHelper with name:"+helperConfig.getName()+" retured "+aCode.isSuccess()+" for init()");
				
				if(aCode.isSuccess())
				{
					log.info("Adding dbConnectionHelper for DB: "+helperConfig.getName()+" to map" );
					dbConnectionHelperMap.put(helperConfig.getName(), helper);
				}
			}
		}
		catch(Exception exp)
		{
			log.error("Error in init", exp);
		}
		log.info("init() end "+code.isSuccess());
		return code;
	}
	
	@Override
	public Connection getConnection(String name) throws SQLException
	{
		DBConnectionHelper helper = dbConnectionHelperMap.get(name);
		if(helper == null)
			throw new IllegalStateException("Unable to find helper to create DB connection for name: "+name);
		Connection conn = helper.getConnection();
		return conn;
	}

}
