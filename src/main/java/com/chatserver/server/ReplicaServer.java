package com.chatserver.server;

public class ReplicaServer extends ComponentServer {
	public ReplicaServer(String serverName, int serverPort) {
		super(serverName, serverPort, ReplicaWorker.class);
	}
	
	
}
