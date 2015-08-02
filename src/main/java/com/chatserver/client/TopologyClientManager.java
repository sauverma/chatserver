package com.chatserver.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.chatserver.bean.ChatServer;
import com.chatserver.bean.Topology;
import com.chatserver.configuration.Configuration;
import com.chatserver.utils.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopologyClientManager {

	private static final Logger logger = Logger.getLogger(TopologyClientManager.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	private static TopologyClientManager topoManager = new TopologyClientManager();

	private static Random random = new Random();

	private TopologyClientManager() {
	}

	public static TopologyClientManager getTopologyClientManager() {
		return topoManager;
	}

	public boolean updateTopology(ChatServer cs) {
		if (Topology.getTopology().hasChatServer(cs) == false) {
			Topology.getTopology().addChatServer(cs);

			List<ChatServer> newServers = null;

			Socket socket = Utils.getSocketConnection(cs.getHost(), cs.getTopologyPort());
			String cmd = "shareTopology";
			String payload = "";
			String reqId = Configuration.getConfiguration().getBrokerId() + "_" + Math.abs(random.nextInt());

			try {
				Collection<ChatServer> servers = new ArrayList<ChatServer>();
				servers.addAll(Topology.getTopology().getChatServers());
				servers.add(new ChatServer(Configuration.getConfiguration()));

				payload = mapper.writeValueAsString(servers);
			} catch (JsonProcessingException e) {
				logger.error(e);
				return false;
			}

			Future<String> neighbourTopology = executorService.submit(new ComponentClient(reqId, socket, cmd, payload));

			try {
				String neighbourJson = neighbourTopology.get();
				neighbourJson = neighbourJson.substring(neighbourJson.indexOf("^") + 1, neighbourJson.length());
				logger.debug(neighbourJson);
				
				try {
					List<ChatServer> neighbourServers = mapper.readValue(neighbourJson.getBytes(), new TypeReference<List<ChatServer>>() {
					});

					for (ChatServer neighbourServer : neighbourServers) {
						if (Topology.getTopology().hasChatServer(neighbourServer) == false && !neighbourServer.getBrokerId().equals(Configuration.getConfiguration().getBrokerId())) {
							Topology.getTopology().addChatServer(neighbourServer);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
					logger.error(e);
					return false;
				} catch (JsonMappingException e) {
					e.printStackTrace();
					logger.error(e);
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e);
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e);
				return false;
			} catch (ExecutionException e) {
				e.printStackTrace();
				logger.error(e);
				return false;
			} finally {
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
		}

		return true;
	}
}
