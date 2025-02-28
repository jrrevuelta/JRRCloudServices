package org.jrrevuelta.cloudservices.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


public class S3ObjectManagement {
	
	private AWSContext awsContext;
	@SuppressWarnings("unused") private S3Client s3Client;
	
	
	/// Constructors and Initializers
	
	public S3ObjectManagement(AWSContext context) {
		super();
		
		// Save deployment configuration for AWS KMS Service
		this.awsContext = context;
		
		// Use credentials of provided IAM userId in deployment file to prepare S3 Client object
		AwsCredentials credentials = AwsBasicCredentials.create(this.awsContext.getIamUserAccessKeyId(), 
															    this.awsContext.getIamUserSecretAccessKey());
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		this.s3Client = S3Client.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.of(this.awsContext.getAwsRegion()))
				.build();
	}
	
}
