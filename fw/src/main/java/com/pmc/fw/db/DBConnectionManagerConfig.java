package com.pmc.fw.db;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="db-connection-manager-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBConnectionManagerConfig 
{
	@XmlElement(name="db-connection-helper-classname")
	private String dbConnectionHelperClassname;
	
	@XmlElement(name="db-connection-helper-config")
	private ArrayList<DBConnectionHelperConfig> configList;

	public ArrayList<DBConnectionHelperConfig> getConfigList() {
		return configList;
	}

	public void setConfigList(ArrayList<DBConnectionHelperConfig> configList) {
		this.configList = configList;
	}

	public String getDbConnectionHelperClassname() {
		return dbConnectionHelperClassname;
	}

	public void setDbConnectionHelperClassname(String dbConnectionHelperClassname) {
		this.dbConnectionHelperClassname = dbConnectionHelperClassname;
	}
	
	
	
 }
