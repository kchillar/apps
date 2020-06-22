package com.pmc.aws.client.helper;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.aws.client.AWSClientResource;
import com.pmc.aws.client.ResourceProviderWrapper;
import com.pmc.fw.model.ResponseCode;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest;
import software.amazon.awssdk.services.lambda.model.CreateFunctionResponse;
import software.amazon.awssdk.services.lambda.model.FunctionCode;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse;

public class LambdaHelper 
{
	public static Logger log = LoggerFactory.getLogger(LambdaHelper.class);

	public LambdaHelper()
	{
	}

	private final LambdaClient getLambdaClient()
	{
		AWSClientResource resource =  ResourceProviderWrapper.getAWSClientResources();
		LambdaClient lClient = LambdaClient.builder().credentialsProvider(resource.getCredentialProvider()).region(resource.getRegion()).build();
		return lClient;
	}

	public ResponseCode listFunctions()
	{
		ResponseCode code = new ResponseCode();
		log.info("listFunctions() start");		
		LambdaClient lClient = getLambdaClient();
		try
		{
			ListFunctionsResponse functionsResponse = lClient.listFunctions();
			
			
			List<FunctionConfiguration> list = functionsResponse.functions();

			for (FunctionConfiguration fc: list) 
			{                
				System.out.println("*** FunctionName: "+fc.functionName()+" state: "+fc.state());
			}	

			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
		}		
		log.info("listFunctions() end");

		return code;
	}

	public ResponseCode createLambdaFunction(Map<String, Object> map)
	{
		log.info("createLambdaFunction() start");
		ResponseCode code = new ResponseCode();		
		LambdaClient lClient = getLambdaClient();
		try		
		{
			String bucketName = (String) map.get("S3CodeBucketName");
			String key = (String) map.get("keyForCodeInS3Bucket");
			String functionaName = (String) map.get("lambdaFunctionName");
			String handlerName = (String) map.get("javaPackageClassFunctionName");			
			String roleArn = (String) map.get("roleArnUnderWhichThisLambdaRuns");//"arn:aws:iam::037263000071:role/LambdaTestRole";
			String runtime = (String) map.get("runtime"); //java11
			String version = (String) map.get("version");
			FunctionCode fCode = FunctionCode.builder()
					.s3Bucket(bucketName)
					.s3Key(key)
					//.s3ObjectVersion(version)
					//.zipFile(zipFile)
					.build(); 

			CreateFunctionRequest req = CreateFunctionRequest.builder()
					.functionName(functionaName)					
					.handler(handlerName)
					.role(roleArn)
					.runtime(runtime)
					.code(fCode)					
					.build();
			CreateFunctionResponse res = lClient.createFunction(req);	
			
			res.functionArn();
			res.memorySize();

			System.out.println("****** creadted function: "+res.functionName());
			code.setSuccess(true);			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
		}
		log.info("createLambdaFunction() end");
		return code;
	}

	public ResponseCode executeLambdaFunction(Map<String, Object> map)
	{
		log.info("executeLambdaFunction() start");
		ResponseCode code = new ResponseCode();		
		LambdaClient lClient = getLambdaClient();
		try		
		{
			String functionaName = (String) map.get("lambdaFunctionName");
			String inputFile = (String) map.get("filePathForInputMsg");

			String inMsg = Util.getStringFromFile(inputFile);
			
			//Need a SdkBytes instance for the payload
			SdkBytes payload = SdkBytes.fromUtf8String(inMsg);

			//Setup an InvokeRequest
			InvokeRequest request = InvokeRequest.builder()
					.functionName(functionaName)
					.payload(payload)
					.build();

			InvokeResponse res = lClient.invoke(request);

			//Get the response
			String value = res.payload().asUtf8String() ;

			//write out the response
			System.out.println("*****Response Lambda: "+functionaName+" Gave response:\n"+value);

			code.setSuccess(true);			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
		}
		log.info("executeLambdaFunction() end");
		return code;
	}

	public ResponseCode deleteLambdaFunction(Map<String, Object> map)
	{
		log.info("deleteLambdaFunction() start");
		ResponseCode code = new ResponseCode();		
		LambdaClient lClient = getLambdaClient();
		try		
		{
			String functionaName = (String) map.get("lambdaFunctionName");
			code.setSuccess(true);			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
		}
		log.info("deleteLambdaFunction() end");
		return code;
	}

}
