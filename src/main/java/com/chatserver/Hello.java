package com.chatserver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.sun.istack.internal.logging.Logger;

@Path("/hello")
public class Hello {

	private static final Logger logger = Logger.getLogger(Hello.class);
	
	@GET
	@Path("/{param}") 
	public Response getMsg(@PathParam("param") String msg) {
		logger.info("served : " + msg);
		return Response.status(200).entity("Jersey say : " + msg).build();
	}
}
