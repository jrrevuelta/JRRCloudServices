package org.jrrevuelta.cloudservices.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

public class KMSEncryptor {
	
	private AWSContext context;
	private byte[] plaintext;
	private String kmsKeyId;
	
	private KmsClient kms;
	
	
	public KMSEncryptor(AWSContext context) {
		
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
	
	public byte[] getPlaintext() {
		return this.plaintext;
	}
	public void setPlaintext(byte[] tokenKey) {
		this.plaintext = tokenKey;
	}
	
	public String getKmsKeyId() {
		return this.kmsKeyId;
	}
	public void setTokenId(String kmsKeyId) {
		this.kmsKeyId = kmsKeyId;
	}
	
	public byte[] getEncryptedTokenKey() {
		
		// Check everything has been appropriately set before calling this method.
		if ((plaintext != null && plaintext.length > 0) &&
			(kmsKeyId != null && !kmsKeyId.isEmpty())) {
				// conditions are ok
			// guard - 
		} else {
			return null;
		}

		// Prepare AWS-KMS EncryptRequest object
		EncryptRequest encReq = EncryptRequest.builder()
				.keyId(this.kmsKeyId)
				.plaintext(SdkBytes.fromByteArray(this.plaintext))
				.build();

		// Process encryption using AWS-KMS
		EncryptResponse encResp = kms.encrypt(encReq);
		SdkBytes ciphertext = encResp.ciphertextBlob();
		return ciphertext.asByteArray();
	}
	
}
