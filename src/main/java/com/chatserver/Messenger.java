package com.chatserver;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.chatserver.bean.Message;
import com.chatserver.bean.MessageStore;
import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/messaging")
public class Messenger {

	private static final Logger logger = Logger.getLogger(Messenger.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	@POST
	@Path("/{producer}/send/{consumer}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response sendMessage(@PathParam("producer") String producer, @PathParam("consumer") String consumer, String message) {
		boolean value = MessageStore.getMessageStore().saveMessages(producer, consumer, DateTime.now(), message);
		
		if (value == true)
			return Response.status(200).entity(producer + " : " + message).build();
		else
			return Response.status(500).entity("Message push failed").build();
	}
	
	@POST
	@Path("/{consumer}/receive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("consumer") String consumer) {
		List<Message> messages = MessageStore.getMessageStore().getMessages(consumer);
		try {
			return Response.status(200).entity(mapper.writeValueAsString(messages)).build();
		} catch (JsonProcessingException e) {
			return Response.status(500).entity(e).build();
		}
	}
	
	@GET
	@Path("/configuration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConfiguration() {
		String configuration = "";
		try {
			configuration = mapper.writeValueAsString(Configuration.getConfiguration());
		} catch (JsonProcessingException e) {
			return Response.status(500).entity(e).build();
		}
		
		return Response.status(200).entity(configuration).build();
	}
}
