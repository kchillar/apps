package com.pmc.fw.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.pmc.fw.model.ResponseCode;

public interface DBConnectionHelper 
{
	public ResponseCode init(DBConnectionHelperConfig helperConfig);
	public Connection getConnection() throws SQLException;
}
