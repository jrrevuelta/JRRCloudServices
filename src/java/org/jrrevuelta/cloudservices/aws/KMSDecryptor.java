package org.jrrevuelta.cloudservices.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

public class KMSDecryptor {
	
	private AWSContext context;
	private byte[] cyphertext;
	private String kmsKeyId;
	
	private KmsClient kms;
	
	
	public KMSDecryptor(AWSContext context) {
		
		// Save deployment configuration for AWS KMS Service
		this.context = context;

		// Use credentials of provided IAM userId in deployment file to prepare KMS Client object
		AwsCredentials credentials = AwsBasicCredentials.create(this.context.getIamUserAccessKeyId(), 
															    this.context.getIamUserSecretAccessKey());
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		this.kms = KmsClient.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.of(this.context.getAwsRegion()))
				.build();
	}
	
	public byte[] getCyphertext() {
		return this.cyphertext;
	}
	public void setCyphertext(byte[] cyphertext) {
		this.cyphertext = cyphertext;
	}
	
	public String getKmsKeyId() {
		return kmsKeyId;
	}
	public void setKmsKeyId(String kmsKeyId) {
		this.kmsKeyId = kmsKeyId;
	}
	
	public byte[] getPlaintext() {
		
		// Check everything has been appropriately set before calling this method.
		if ((cyphertext != null && cyphertext.length > 0) &&
			(kmsKeyId != null && !kmsKeyId.isEmpty())) {
				// conditions are ok
			// guard - 
		} else {
			return null;
		}
		
		// Prepare AWS-KMS DecryptRequest object
		DecryptRequest decReq = DecryptRequest.builder()
				.keyId(this.kmsKeyId)
				.ciphertextBlob(SdkBytes.fromByteArray(this.cyphertext))
				.build();
		
		// Process decryption using AWS-KMS
		DecryptResponse decResp = kms.decrypt(decReq);
		SdkBytes plaintext = decResp.plaintext();
		return plaintext.asByteArray();
	}
	
}
