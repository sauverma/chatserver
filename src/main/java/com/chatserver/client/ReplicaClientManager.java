package com.chatserver.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.chatserver.bean.ChatServer;
import com.chatserver.bean.Message;
import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReplicaClientManager {
	private static final Logger logger = Logger.getLogger(ReplicaClientManager.class);
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	private static ReplicaClientManager rcManager = new ReplicaClientManager();
	
	private static Random random = new Random();
	
	private ReplicaClientManager() {}
	
	public static ReplicaClientManager getReplicaClientManager() {
		return rcManager;
	}
	
	public boolean broadcastReplicaCopyRequest(Collection<ChatServer> chatServers, Message message) {
		for (ChatServer chatServer : chatServers) {
			/*
			 * send the replication request on the replicaPort of the chatServer
			 */
			
			String reqId = Configuration.getConfiguration().getBrokerId() + "_" + random.nextLong();
			
			Map<String, List<Message>> messages = new HashMap<String, List<Message>>();
			messages.put(message.getConsumer(), Arrays.asList(message));
			
			String cmd;
			try {
				cmd = mapper.writeValueAsString(messages);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e);
				return false;
			}
			
			Future<String> response = executorService.submit(new ComponentClient(reqId, chatServer.getHost(), chatServer.getReplicaPort(), cmd));
			try {
				logger.debug("Replica copy Request : " + response.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
