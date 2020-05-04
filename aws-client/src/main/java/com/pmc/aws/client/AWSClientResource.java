									package com.pmc.aws.client;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.resources.Resource;
import com.pmc.fw.xml.XMLHelper;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class AWSClientResource implements Resource 
{
	public static Logger log = LoggerFactory.getLogger(AWSClientResource.class);
	private AWSClientConfig config;
	private Region region;
	private AwsCredentialsProvider credentialProvider;
	private AwsBasicCredentials credentials;

	
	public AWSClientResource()
	{		
	}
	
	@Override
	public ResponseCode init(InputStream configStream) 
	{
		ResponseCode code = new ResponseCode();
		
		try
		{
			AWSClientConfig aConfig = (AWSClientConfig) XMLHelper.getObjectFromInputStream(AWSClientConfig.class, configStream);
			this.setConfig(aConfig);			
			setRegion(Region.of(config.getRegionConfig().getName()));
			credentials = AwsBasicCredentials.create(config.getAccessKey().getKeyId(),config.getAccessKey().getsKeyId());
			setCredentialProvider(StaticCredentialsProvider.create(credentials));
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		
		return code;
	}


	public AWSClientConfig getConfig() 
	{
		return config;
	}

	public AwsCredentialsProvider getCredentialProvider()
	{
		return credentialProvider;
	}
	
	public AwsCredentials getCredentials() 
	{
		return credentials;
	}

	public Region getRegion() {
		return region;
	}

	public void setConfig(AWSClientConfig config) 
	{
		this.config = config;
	}

	public void setRegion(Region region) 
	{
		this.region = region;
	}
	
	public void setCredentialProvider(AwsCredentialsProvider credentialProvider) {
		this.credentialProvider = credentialProvider;
	}	


}
