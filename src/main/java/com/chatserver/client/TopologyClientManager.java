package com.chatserver.client;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.chatserver.bean.ChatServer;
import com.chatserver.bean.Topology;
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
			
		}
		
		return true;
	}
}
