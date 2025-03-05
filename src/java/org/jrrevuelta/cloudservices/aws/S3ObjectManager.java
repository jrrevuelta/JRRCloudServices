package org.jrrevuelta.cloudservices.aws;

import java.util.List;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;


public class S3ObjectManager {
	
	private AWSContext awsContext;
	private S3Client s3Client;
	
	private String contentType;
	private List<Tag> tags;
	private String folder;
	
	private static final Logger log = Logger.getLogger("org.jrrevuelta.cloudservices.aws.S3ObjectManager");
	
	
	/// Constructors and Initializers
	
	public S3ObjectManager(AWSContext context) {
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
	
	/// Business Functionality
	
	public void putS3Object(String bucketName, String objectKey, byte[] contents) {
		
		try {
			String objectFullName = objectKey;
			if (folder != null && !folder.isEmpty()) {
				objectFullName = folder + objectKey;
			}
			PutObjectRequest.Builder builder = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(objectFullName);
			if (contentType != null && !contentType.isEmpty()) {
				builder.contentType(contentType);
			}
			if (tags != null && !tags.isEmpty()) {
				Tagging allTags = Tagging.builder()
						.tagSet(tags)
						.build();
				builder.tagging(allTags);
			}
			
			PutObjectRequest putObjectRequest = builder.build();
			this.s3Client.putObject(putObjectRequest, RequestBody.fromBytes(contents));
			
		} catch (S3Exception e) {
			
			log.severe("JRR: Problem with the AWS S3 service request: " + e.getMessage());
		}
	}
	
	/// Public Accessors
	
	public String getContentType() { return contentType; }
	public void setContentType(String contentType) { this.contentType = contentType; }
	
	public List<Tag> getTags() { return tags; }
	public void setTags(List<Tag> tags) { this.tags = tags; }
	
	public String getFolder() { return folder; }
	public void setFolder(String folder) { this.folder = folder; }
	
}
