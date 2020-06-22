package com.pmc.utils.diffdb;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="db-table")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBTableInfo 
{
	@XmlAttribute(name="name")
	private String name;
	
	@XmlElement(name="db-column")
	private ArrayList<DBColumnInfo> columns;

	public DBTableInfo()
	{		
	}
	
	public DBTableInfo(String name)
	{
		setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<DBColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<DBColumnInfo> columns) {
		this.columns = columns;
	}

	public void addColumnInfo(DBColumnInfo columnInfo)
	{
		if(getColumns() == null)
			setColumns(new ArrayList<>());
		
		getColumns().add(columnInfo);
	}

	public String toString()
	{
		StringBuilder buff = new StringBuilder();
		
		buff.append("{TableName: "+getName());
		
		if(getColumns() != null && getColumns().size() > 0)
		{
			buff.append(", columns:[\n");			
			for(DBColumnInfo col: getColumns())
				buff.append("   "+col.toString());			
			buff.append(" ]\n");
			buff.append("}");
		}
		else
			buff.append("}");
		
		return buff.toString();
	}
	
}
