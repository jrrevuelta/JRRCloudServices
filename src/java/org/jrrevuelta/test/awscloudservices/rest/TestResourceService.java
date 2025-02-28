package org.jrrevuelta.test.awscloudservices.rest;

import java.util.Base64;

import org.jrrevuelta.cloudservices.CloudServiceContext;
import org.jrrevuelta.cloudservices.aws.AWSContext;
import org.jrrevuelta.cloudservices.aws.KMSCypher;

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
	
	
	public TestResourceService() {
		super();
	}
	
	@Override
	public Response getDocsList() {
		return null;
	}

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
	
}
