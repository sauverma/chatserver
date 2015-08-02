package com.chatserver.bean;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 
 * @author sauverma
 *
 * This class holds the topology means which chatServers are connected to form a cluster
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
	
	public boolean hasChatServer(ChatServer cs) {
		return chatServers.contains(cs);
	}
	
	public void addChatServer(ChatServer cs) {
		chatServers.put(cs.getBrokerId(), cs);
	}
	
	public ChatServer getChatServer(String csId) {
		return chatServers.get(csId);
	}
	
	public void removeChatServer(String csId) {
		chatServers.remove(csId);
	}
	
	public Collection<ChatServer> getChatServers() {
		return chatServers.values();
	}
}
