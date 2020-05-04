package com.pmc.aws.client.helper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmc.aws.client.AWSClientResource;
import com.pmc.aws.client.ResourceProviderWrapper;
import com.pmc.aws.client.view.S3ViewEventHandler;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;


public class S3Helper 
{
	public static Logger log = LoggerFactory.getLogger(S3ViewEventHandler.class);

	public S3Helper()
	{
	}

	private final S3Client getS3Client()
	{
		AWSClientResource resource =  ResourceProviderWrapper.getAWSClientResources();
		S3Client s3 = S3Client.builder().credentialsProvider(resource.getCredentialProvider()).region(resource.getRegion()).build();
		return s3;
	}

	public void listBuckets()
	{
		log.info("listBuckets() start");		
		S3Client s3 = getS3Client();
		try
		{
			//List buckets
			ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
			ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
			listBucketsResponse.buckets().stream().forEach(x -> System.out.println("Got bucket: "+x.name()));
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
			//s3.close();
		}
		log.info("listBuckets() end");
	}

	/**
	 * regionId
	 * bucketName	
	 * @param map
	 */
	public void createBucket(Map<String, Object> map)
	{
		log.info("createBuckets() start");
		S3Client s3 = getS3Client();
		try
		{
			String regionId = (String) map.get("regionId");		
			String bucket = (String) map.get("bucketName");
			log.info("Creating bucket: "+bucket+" in region id: "+regionId);

			// Create bucket
			CreateBucketRequest createBucketRequest = CreateBucketRequest
					.builder()
					.bucket(bucket)
					.createBucketConfiguration(CreateBucketConfiguration.builder()
							.locationConstraint(regionId)
							.build())
					.build();
			s3.createBucket(createBucketRequest);
		}
		catch(Exception exp)
		{
			log.error("ERROR:", exp);
		}
		finally
		{
			//s3.close();
		}		
		log.info("createBuckets() end");
	}

	
	public void deleteBucket(Map<String, Object> map)
	{
		log.info("deleteBucket() start");
		S3Client s3 = getS3Client();
		try
		{	
			String bucket = (String) map.get("bucketName");
			log.info("Deleting S3 bucket: "+bucket);
			DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket).build();
	        s3.deleteBucket(deleteBucketRequest);
	        log.info("Deleted S3 bucket: "+bucket);
		}
		catch(Exception exp)
		{
			log.error("ERROR:", exp);
		}
		finally
		{
			//s3.close();
		}		
		log.info("deleteBucket() end");
	}

	
	public void listKeys(Map<String, Object> map)
	{
		log.info("listFolders() start");		
		S3Client s3 = getS3Client();
		String bucketName = (String) map.get("bucketName");

		try 
		{
			ListObjectsRequest listObjects = ListObjectsRequest
					.builder()
					.bucket(bucketName)
					.build();

			ListObjectsResponse res = s3.listObjects(listObjects);
			Iterator<S3Object> it = res.contents().iterator();

			while(it.hasNext())
			{
				S3Object myValue = (S3Object) it.next();
				log.info("key: " + myValue.key()+" valueSize: "+calKb(myValue.size())+" KB, owner: "+myValue.owner());
			}
		}
		catch (S3Exception e) 
		{
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		finally
		{
			//s3.close();
		}
		log.info("listFolders() end");
	}

	private static long calKb(Long val) 
	{
		return val/1024;
	}

	public void createAKeyInBucket(Map<String, Object> map)
	{
		log.info("createAKeyInBucket() start");
		S3Client s3 = getS3Client();
		try
		{
			String bucketName = (String) map.get("bucketName");
			String key = (String) map.get("key");
			String fileName = (String) map.get("filePathToUpload");
			File file = new File(fileName);
			log.info("Creating key: "+key+" in bucket: "+bucketName+" with content from file: "+file.getPath());			
			PutObjectRequest putRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();			
			s3.putObject(putRequest, RequestBody.fromFile(file));
			//s3.com
			log.info("Uploaded: "+file.length());
		}
		catch(Exception exp)
		{
			log.error("ERROR:", exp);
		}
		finally
		{
			//s3.close();
		}
		log.info("createAKeyInBucket() end");
	}

	
	public void getContentOfS3Key(Map<String, Object> map)
	{
		log.info("getContentOfS3Key() start");
		S3Client s3 = getS3Client();

		try
		{
			String bucketName = (String) map.get("bucketName");
			String key = (String) map.get("key");
			String fileName = (String) map.get("downloadContentToFile");
			File file = new File(fileName);

			log.info("bucketName:"+bucketName+", key:"+key+", downloadTo:"+file.getPath());			
			s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
	                ResponseTransformer.toFile(file));			
			log.info("File size: "+file.length());			
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}
		log.info("getContentOfS3Key() end");
	}
	
	public void deleteKeyInS3Bucket(Map<String, Object> map)
	{
		log.info("deleteKeyInS3Bucket() start");
		S3Client s3 = getS3Client();
		try
		{	
			String bucket = (String) map.get("bucketName");
			String key = (String) map.get("key");
			log.info("Deleting key "+key+" in S3 bucket: "+bucket);
			
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
	        s3.deleteObject(deleteObjectRequest);
	        log.info("Deleted key "+key+" in S3 bucket: "+bucket);
		}
		catch(Exception exp)
		{
			log.error("ERROR:", exp);
		}
		finally
		{
			//s3.close();
		}		
		log.info("deleteKeyInS3Bucket() end");
	}

	
	public void createAKeyInBucketWithFileContent(Map<String, Object> map)
	{
		log.info("createAKeyInBucketWithFileContent() start");
		S3Client s3 = getS3Client();
		try
		{
			String bucketName = (String) map.get("bucketName");
			String key = (String) map.get("key");
			String fileName = (String) map.get("filePathToUpload");
			File file = new File(fileName);
			log.info("Creating key: "+key+" in bucket: "+bucketName+" with content from file: "+file.getPath());
			
			CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
	                .bucket(bucketName).key(key)
	                .build();
	        CreateMultipartUploadResponse response = s3.createMultipartUpload(createMultipartUploadRequest);
	        String uploadId = response.uploadId();
	        System.out.println("Using uploadId: "+uploadId);

	        FileInputStream fis = new FileInputStream(file);
	        
	        byte buffer[] = new byte[1024*1024*6];
	        int read =0 ;
	        int total = 0;
	        int partNo = 1;
	        
	        List<CompletedPart> list = new LinkedList<CompletedPart>();
	        
	        while( (read=fis.read(buffer, 0, buffer.length)) > 0)
	        {	        	
	        	log.info(partNo+" Read bytes: "+read);
	        	byte[] data = new byte[read];
	        	
	        	System.arraycopy(buffer, 0,data, 0, read);
	        	
	        	UploadPartRequest uploadPartRequest = UploadPartRequest.builder().bucket(bucketName).key(key)
	                    .uploadId(uploadId)
	                    .partNumber(partNo).build();
	            String etag = s3.uploadPart(uploadPartRequest, RequestBody.fromBytes(data)).eTag();
	            CompletedPart part = CompletedPart.builder().partNumber(partNo).eTag(etag).build();
	            list.add(part);
	            total =+ read;
	        	partNo++;	        	
	        }	        	        
	        fis.close();
	        
	        CompletedMultipartUpload.Builder builder = CompletedMultipartUpload.builder();
	        builder.parts(list);	        	        
	        CompletedMultipartUpload completedMultipartUpload = builder.build();	        
	        CompleteMultipartUploadRequest completeMultipartUploadRequest =
	                CompleteMultipartUploadRequest.builder().bucket(bucketName).key(key).uploadId(uploadId)
	                        .multipartUpload(completedMultipartUpload).build();
	        s3.completeMultipartUpload(completeMultipartUploadRequest);
	        
			//s3.com
			log.info("Completed multi upload with parts: "+partNo+" with total size: "+total+" fileSize: "+file.length()+" and uploadId: "+uploadId);
		}
		catch(Exception exp)
		{
			log.error("ERROR:", exp);
		}
		finally
		{
			//s3.close();
		}
		log.info("createAKeyInBucketWithFileContent() end");
	}

}