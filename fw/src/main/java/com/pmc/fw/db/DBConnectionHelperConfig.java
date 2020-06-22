package com.pmc.fw.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="db-connection-helper-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBConnectionHelperConfig 
{
	@XmlAttribute(name="name")
	private String name;
	@XmlElement(name="db-url")
	private String url;
	@XmlElement(name="username")
	private String username;
	@XmlElement(name="password")
	private String password;
	@XmlElement(name="driver-class")
	private String classname;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	
}
