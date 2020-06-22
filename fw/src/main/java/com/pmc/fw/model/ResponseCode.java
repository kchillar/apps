package com.pmc.fw.model;

public class ResponseCode<T> 
{
	private boolean isSuccess = false;
	private String code;
	private String msg;
	private T object;
	
	public ResponseCode()
	{		
	}
	
	public ResponseCode(String code, String msg)
	{
		setCode(code);
		setMsg(msg);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
	
	
	
}
