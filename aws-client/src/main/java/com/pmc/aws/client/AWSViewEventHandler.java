package com.pmc.aws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.aws.client.s3.S3Helper;
import com.pmc.fw.model.ResponseCode;
import com.pmc.fw.view.ViewEvent;
import com.pmc.fw.view.ViewEventHandler;

public class AWSViewEventHandler implements ViewEventHandler 
{
	public static Logger log = LoggerFactory.getLogger(AWSViewEventHandler.class);
	
	private static final String ListS3Bucket = "List-S3-Bucket";
	private static final String CreateS3Bucket = "Create-S3-Bucket";
	private static final String CreateKeyInS3Bucket = "Create-Key-In-S3-Bucket";
	private static final String ListKeysInS3Bucket = "List-Keys-In-S3-Bucket";
	private static final String CreateKeyInS3BucketMultipart = "Create-Key-In-S3-Bucket-Multipart";
	private static final String GetContentOfS3KeyToFile = "Get-Content-For-Key-In-S3-Bucket";
	private static final String DeleteKeyInS3Bucket = "Delete-Key-In-S3-Bucket";
	private static final String DeleteS3Bucket = "Delete-S3-Bucket";
	
	
	private static S3Helper s3helper = new S3Helper();

	@Override
	public ResponseCode handleEvent(ViewEvent event) 
	{
		ResponseCode code = new ResponseCode();

		String eventId = event.getEventId();
		
		log.info("Event: "+eventId+" params: "+event.getEventData());

		switch(eventId)
		{
		case ListS3Bucket:
			log.info("List S3 Buckets");
			s3helper.listBuckets();
			break;
		case CreateS3Bucket:
			log.info("Create S3 bucket");
			s3helper.createBucket(event.getEventData());
			break;
		case ListKeysInS3Bucket:
			log.info("List keys in a S3 bucket");
			s3helper.listKeys(event.getEventData());
			break;
		case DeleteS3Bucket:
			log.info("Delete S3 bucket");
			s3helper.deleteBucket(event.getEventData());
			break;									
		case CreateKeyInS3Bucket:
			log.info("S3 Create key in a S3 bucket");
			s3helper.createAKeyInBucket(event.getEventData());
			break;
		case CreateKeyInS3BucketMultipart:
			log.info("S3 Create key in a S3 bucket with multi part");
			s3helper.createAKeyInBucketWithFileContent(event.getEventData());
			break;			
		case GetContentOfS3KeyToFile:
			log.info("Get S3 Key Content to File");
			s3helper.getContentOfS3Key(event.getEventData());
			break;		
		case DeleteKeyInS3Bucket:
			log.info("Delete Key in S3 bucket");
			s3helper.deleteKeyInS3Bucket(event.getEventData());
			break;									
			
		default:
			log.info("default");
		}

		log.info("completed event handling");
		
		return code;
	}

}
