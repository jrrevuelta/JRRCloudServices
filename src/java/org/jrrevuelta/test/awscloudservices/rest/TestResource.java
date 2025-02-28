package org.jrrevuelta.test.awscloudservices.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
	
	@GET
	@Path("/docs")
	public Response getDocsList();
	
	@POST
	@Path("/enc")
	public Response getEncStr(String plaintext);
	
	@POST
	@Path("/dec")
	public Response getDecStr(String cyphertext);
	
}
