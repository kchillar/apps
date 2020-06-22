package com.pmc.fw.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.pmc.fw.resources.Resource;

public interface DBConnectionManager extends Resource
{
	public Connection getConnection(String name) throws SQLException;
}
