package com.hippo;

import java.io.IOException;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.*;
import java.io.*;

import org.apache.http.entity.ContentType;

/**
 * 本程式是由AWS S3 Developer Guide中的範例程式修改而來
 * 
 * @see <a
 *      href="http://docs.aws.amazon.com/AmazonS3/latest/dev/PresignedUrlUploadObjectDotNetSDK.html">Reference: Upload
 *      an Object Using Pre-Signed URL - AWS SDK for Java</a>
 */
public class S3Sample {
	private static String bucketName = "<your bucket name>";
	private static String objectKey = "<your filename on s3>";

	public static void main(String[] args) throws IOException {
		AmazonS3 s3client = new AmazonS3Client(
				new PropertiesCredentials(S3Sample.class
						.getResourceAsStream("AwsCredentials.properties")));

		// 需要加上bucket的region,否則很可能會出錯
		s3client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));

		try {
			System.out.println("Generating pre-signed URL.");
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
					bucketName, objectKey);

			// 上傳檔案的method要用PUT
			generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
			generatePresignedUrlRequest.setExpiration(expiration);

			// 需加上ContentType,如用curl就用"application/x-www-form-urlencoded"
			generatePresignedUrlRequest
					.setContentType("application/x-www-form-urlencoded");

			URL url = s3client
					.generatePresignedUrl(generatePresignedUrlRequest);

			System.out.println("Pre-Signed URL = " + url.toString());
		} catch (AmazonServiceException exception) {
			System.out.println("Caught an AmazonServiceException, "
					+ "which means your request made it "
					+ "to Amazon S3, but was rejected with an error response "
					+ "for some reason.");
			System.out.println("Error Message: " + exception.getMessage());
			System.out.println("HTTP  Code: " + exception.getStatusCode());
			System.out.println("AWS Error Code:" + exception.getErrorCode());
			System.out.println("Error Type:    " + exception.getErrorType());
			System.out.println("Request ID:    " + exception.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, "
					+ "which means the client encountered "
					+ "an internal error while trying to communicate"
					+ " with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}
