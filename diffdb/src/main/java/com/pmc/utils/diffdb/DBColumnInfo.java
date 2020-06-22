package com.pmc.utils.diffdb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="db-column")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBColumnInfo 
{
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="value")
	private String value;
	@XmlAttribute(name="data-type")
	private String dataType;
	
	@XmlAttribute(name="column-size")
	private int columnSize;
	
	@XmlAttribute(name="diff-ignore")
	private boolean ignoreValueForDiff = false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isIgnoreValueForDiff() {
		return ignoreValueForDiff;
	}
	public void setIgnoreValueForDiff(boolean ignoreValueForDiff) {
		this.ignoreValueForDiff = ignoreValueForDiff;
	}
	
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}
	public String toString()
	{
		return "{col-name:"+getName()+" dataType: "+getDataType()+"col-size: "+getColumnSize()+", value: "+getValue()+"}";
	}
	
}
