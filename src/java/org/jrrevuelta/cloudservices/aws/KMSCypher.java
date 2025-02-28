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
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;


public class KMSCypher {
	
	private AWSContext awsContext;
	private KmsClient kmsClient;
	
	private byte[] plaintext;
	private byte[] cyphertext;
	private String kmsKeyId;
	
	
	/// Constructors and Initializers
	
	public KMSCypher(AWSContext context) {
		super();
		
		// Save deployment configuration for AWS KMS Service
		this.awsContext = context;

		// Use credentials of provided IAM userId in deployment file to prepare KMS Client object
		AwsCredentials credentials = AwsBasicCredentials.create(this.awsContext.getIamUserAccessKeyId(), 
															    this.awsContext.getIamUserSecretAccessKey());
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		this.kmsClient = KmsClient.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.of(this.awsContext.getAwsRegion()))
				.build();
	}
	
	public KMSCypher(AWSContext context, String kmsKeyId) {
		this(context);
		setKmsKeyId(kmsKeyId);
	}
	
	/// Business Functionality
	
	public byte[] encrypt() {
		
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
		EncryptResponse encResp = kmsClient.encrypt(encReq);
		SdkBytes ciphertext = encResp.ciphertextBlob();
		return ciphertext.asByteArray();
	}
	
	public byte[] decrypt() {
		
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
		DecryptResponse decResp = kmsClient.decrypt(decReq);
		SdkBytes plaintext = decResp.plaintext();
		return plaintext.asByteArray();
	}
	
	/// Public Accessors
	
	public byte[] getPlaintext() { return this.plaintext; }
	public void setPlaintext(byte[] tokenKey) { this.plaintext = tokenKey; }
	
	public byte[] getCyphertext() { return this.cyphertext; }
	public void setCyphertext(byte[] cyphertext) { this.cyphertext = cyphertext; }
	
	public String getKmsKeyId() { return kmsKeyId; }
	public void setKmsKeyId(String kmsKeyId) { this.kmsKeyId = kmsKeyId; }
	
}
