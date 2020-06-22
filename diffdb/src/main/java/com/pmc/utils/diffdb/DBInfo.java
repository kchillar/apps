package com.pmc.utils.diffdb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="db-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBInfo 
{
	@XmlAttribute(name="name")
	private String dbName;

	@XmlElement(name="db-table")
	private List<DBTableInfo> dbTables;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public List<DBTableInfo> getDbTables() {
		return dbTables;
	}

	public void setDbTables(List<DBTableInfo> dbTables) {
		this.dbTables = dbTables;
	}
	
	
}
