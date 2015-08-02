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
	
	/*
	 * Updates offset per consumer across chatServer cluster
	 */
	public boolean broadcastReplicatOffsetUpdate(Collection<ChatServer> chatServers, String consumer, long offset) {
		for (ChatServer chatServer : chatServers) {
			String reqId = Configuration.getConfiguration().getBrokerId() + "_" + Math.abs(random.nextInt());
			
			Map<String, Long> consumerOffset = new HashMap<String, Long> ();
			consumerOffset.put(consumer, offset);
			
			String cmd = "updateOffsets";
			String payload = "";
			try {
				payload = mapper.writeValueAsString(consumerOffset);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e);
				return false;
			}
			
			Future<String> response = executorService.submit(new ComponentClient(reqId, chatServer.getHost(), chatServer.getReplicaPort(), cmd, payload));
			try {
				logger.debug("Replica UpdateOffset Request : " + response.get());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/*
	 * Takes care of broadcasting message updates on a chatserver
	 */
	public boolean broadcastReplicaCopyRequest(Collection<ChatServer> chatServers, Message message) {
		for (ChatServer chatServer : chatServers) {
			/*
			 * send the replication request on the replicaPort of the sibling chatServers
			 */
			
			String reqId = Configuration.getConfiguration().getBrokerId() + "_" + Math.abs(random.nextInt());
			
			Map<String, List<Message>> messages = new HashMap<String, List<Message>>();
			messages.put(message.getConsumer(), Arrays.asList(message));
			
			String cmd = "copy";
			String payload;
			try {
				payload = mapper.writeValueAsString(messages);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
				logger.error(e1);
				return false;
			}
			
			Future<String> response = executorService.submit(new ComponentClient(reqId, chatServer.getHost(), chatServer.getReplicaPort(), cmd, payload));
			try {
				logger.debug("Replica Copy Request : " + response.get());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
