package org.jrrevuelta.cloudservices.aws;

import org.jrrevuelta.cloudservices.CloudServiceContext;

public class AWSContext implements CloudServiceContext {
	
	// IAM Application Service User credentials
	private String iamUserName;
	private String iamUserAccessKeyId;
	private String iamUserSecretAccessKey;
	
	// AWS Services Region 
	private String awsRegion;
	
	
	public AWSContext() { super(); }

	public String getIamUserName() { return iamUserName; }
	public void setIamUserName(String iamUserName) { this.iamUserName = iamUserName; }

	public String getIamUserAccessKeyId() { return iamUserAccessKeyId; }
	public void setIamUserAccessKeyId(String iamUserAccessKeyId) { this.iamUserAccessKeyId = iamUserAccessKeyId; }

	public String getIamUserSecretAccessKey() { return iamUserSecretAccessKey; }
	public void setIamUserSecretAccessKey(String iamUserSecretAccessKey) { this.iamUserSecretAccessKey = iamUserSecretAccessKey; }

	public String getAwsRegion() { return awsRegion; }
	public void setAwsRegion(String awsRegion) { this.awsRegion = awsRegion; }

}
