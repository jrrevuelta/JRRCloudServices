package org.jrrevuelta.cloudservices.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class SNSSendSMS {
	
	private AWSContext context;
	private SnsClient sns;
	private String phoneNumber;
	private String message;
	
	public static final String e164Regex = "^\\+[0-9]{10,15}$"; 
	
	
	public SNSSendSMS(AWSContext context) {
		
		// Save deployment configuration for AWS SNS Service
		this.context = context;

		// Use credentials of provided IAM userId in deployment file to prepare SNS Client object
		AwsCredentials credentials = AwsBasicCredentials.create(this.context.getIamUserAccessKeyId(), 
															    this.context.getIamUserSecretAccessKey());
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

		this.sns = SnsClient.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.of(this.context.getAwsRegion()))
				.build();
	}
	
	public SNSSendSMS(AWSContext context, String phoneNumber, String message) {
		this(context);
		setPhoneNumber(phoneNumber);
		setMessage(message);
	}
	
	public void sendSMS() {
		if (this.phoneNumber != null && this.message != null) {
			PublishRequest request = PublishRequest.builder()
					.phoneNumber(this.phoneNumber)
					.message(this.message)
					.build();
			sns.publish(request);
		}
	}
	
	public void sendSMS(String phoneNumber, String message) {
		if (phoneNumber != null && message != null && phoneNumber.matches(e164Regex)) {
			PublishRequest request = PublishRequest.builder()
					.phoneNumber(phoneNumber)
					.message(message)
					.build();
			sns.publish(request);
		}
	}
	
	public String getPhoneNumber() { return this.phoneNumber; }
	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber.matches(e164Regex)) {
			this.phoneNumber = phoneNumber;
		}
	}
	
	public String getMessage() { return this.message; }
	public void setMessage(String message) {
		this.message = message;
	}
	
}
