package com.chatserver.bean;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 
 * @author sauverma
 *
 * This class holds the which chatServers are connected to form a cluster
 * Also keeps track of chatServers if they are alive or not, remove them if they are not alive
 * 
 */

public class Topology {
	
	private static final Logger logger = Logger.getLogger(Topology.class);
	
	private ConcurrentHashMap<String, ChatServer> chatServers;
	
	private static Topology topology = new Topology();
	
	private Topology() {
		chatServers = new ConcurrentHashMap<String, ChatServer>();
	}
	
	public static Topology getTopology() {
		return topology;
	}
	
	public void addChatServer(ChatServer cs) {
		chatServers.put(cs.getBrokerId(), cs);
	}
	
	public ChatServer getChatServer(String csId) {
		return chatServers.get(csId);
	}
}
