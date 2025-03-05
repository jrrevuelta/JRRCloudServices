package org.jrrevuelta.test.awscloudservices.rest;

import java.net.URI;
import java.util.Base64;
import java.util.logging.Logger;

import org.jrrevuelta.cloudservices.CloudServiceContext;
import org.jrrevuelta.cloudservices.aws.AWSContext;
import org.jrrevuelta.cloudservices.aws.KMSCypher;
import org.jrrevuelta.cloudservices.aws.S3ObjectManager;

import jakarta.annotation.Resource;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;


public class TestResourceService implements TestResource {
	
	@Resource(name="cloud/CloudServicesContext", 
			  type=org.jrrevuelta.cloudservices.CloudServiceContext.class)
	private CloudServiceContext context;
	
	private static final String KMS_KEY_ID = "alias/jrr-testcloudservices-key-001";  /// Needed only to access/use the KMS encrypt/decrypt functions
	/// Actually, in the case of AWS, only this KMS KeyId (or alias) is needed. But in the case of GCP, there are two components: The KMS KeyId and the KeyringId.
	
	private static final String FOLDER = "folder1/";
	
	private static final Logger log = Logger.getLogger("org.jrrevuelta.test.awscloudservices.rest.TestResourceService");
	
	///  Constructors and Initialization
	
	public TestResourceService() {
		super();
		log.fine("JRR: Instantiating TestResourceService.");
	}
	
	///  TestResource interface implementation
	
	/**
	 * Get Encrypted String
	 * 
	 */
	@Override
	public Response getEncStr(String plaintext) {
		
		ResponseBuilder builder = null;
		byte[] cyphertext = null;
		
		if (context instanceof AWSContext) {
			KMSCypher cypher = new KMSCypher((AWSContext)context, KMS_KEY_ID);
			cypher.setPlaintext(plaintext.getBytes());
			cyphertext = cypher.encrypt();
		}
		
		Base64.Encoder b64Encoder = Base64.getUrlEncoder();
		
		if (cyphertext != null) {
			builder = Response.ok(b64Encoder.encodeToString(cyphertext), MediaType.TEXT_PLAIN);
		} else {
			builder = Response.serverError();
		}
		return builder.build();
		
	}
	
	/**
	 * Get De-crypted String
	 * 
	 */
	@Override
	public Response getDecStr(String cyphertext) {
		
		ResponseBuilder builder = null;
		byte[] plaintext = null;
		
		Base64.Decoder b64Decoder = Base64.getUrlDecoder();
		
		if (context instanceof AWSContext) {
			KMSCypher cypher = new KMSCypher((AWSContext)context, KMS_KEY_ID);
			cypher.setCyphertext(b64Decoder.decode(cyphertext));
			plaintext = cypher.decrypt();
		}
		
		if (plaintext != null) {
			builder = Response.ok(new String(plaintext), MediaType.TEXT_PLAIN);
		} else {
			builder = Response.serverError();
		}
		return builder.build();
		
	}
	
	/**
	 * Put Object
	 * 
	 */
	@Override
	public Response putObject(String bucket, String key, String contentType, byte[] contents) {
		
		ResponseBuilder builder = null;
		
		if (context instanceof AWSContext) {
			S3ObjectManager s3Access = new S3ObjectManager((AWSContext)context);
			s3Access.setFolder(FOLDER);
			s3Access.setContentType(contentType);
			s3Access.putS3Object(bucket, key, contents);
			builder = Response.created(URI.create(bucket + "/" + FOLDER + "/" + key));
			
		} else {
			builder = Response.serverError();
		}
		
		return builder.build();
	}
	
}
