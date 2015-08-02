package com.chatserver;

import java.io.IOException;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.chatserver.bean.ChatServer;
import com.chatserver.bean.Topology;
import com.chatserver.client.TopologyClientManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author sauverma
 *
 * Allows to add / remove chatServer via a REST API
 * Addition/removal of CS can be done at a single node (i.e. chatServer),
 * this addition/removal would trigger a topology creation
 * 
 */

@Path("/topology")
public class TopologyService {
	
	private static final Logger logger = Logger.getLogger(TopologyService.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addChatServer(String chatServerJson) {
		ChatServer cs = null;
		try {
			cs = mapper.readValue(chatServerJson.getBytes(), ChatServer.class);
			/*
			 * 
			 * Start topology building trigerred as a part of this addition
			 */
			
			if (cs != null) {
				TopologyClientManager.getTopologyClientManager().updateTopology(cs);
			}
			return Response.status(200).entity("Added chatServer with brokerId " + cs.getBrokerId()).build();
		} catch (JsonParseException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		} 
	}
	
	@Path("/remove")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeChatServer(String chatServerJson) {
		ChatServer cs = null;
		try {
			cs = mapper.readValue(chatServerJson.getBytes(), ChatServer.class);
			Topology.getTopology().removeChatServer(cs.getBrokerId());
			return Response.status(200).entity("Removed chatServer with brokerId " + cs.getBrokerId()).build();
		} catch (JsonParseException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		}
	}
	
	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopology() {
		Collection<ChatServer> csList = Topology.getTopology().getChatServers();
		
		try {
			return Response.status(200).entity(mapper.writeValueAsString(csList)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(e);
			return Response.status(500).entity(e).build();
		}
	}
}
