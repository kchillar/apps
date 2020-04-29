package com.pmc.fw.resources;

import java.io.InputStream;

import com.pmc.fw.model.ResponseCode;

public interface Resource 
{
	public ResponseCode init(InputStream configStream);
}
