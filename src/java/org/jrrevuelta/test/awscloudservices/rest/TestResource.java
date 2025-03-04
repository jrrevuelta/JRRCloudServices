package org.jrrevuelta.test.awscloudservices.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Test Resource:
 * 
 * @author José Ramón Revuelta
 */
@Path("/test")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public interface TestResource {
	
	@POST
	@Path("/enc")
	public Response getEncStr(String plaintext);
	
	@POST
	@Path("/dec")
	public Response getDecStr(String cyphertext);
	
	@POST
	@Path("upload/{bucket}/{key}")
	@Consumes(MediaType.WILDCARD)
	public Response putObject(
			@PathParam("bucket") String bucket,
			@PathParam("key") String key,
			@HeaderParam("Content-Type") String contentType,
			byte[] contents);
	
}
