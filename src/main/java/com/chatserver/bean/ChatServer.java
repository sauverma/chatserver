package com.chatserver.bean;

import com.chatserver.configuration.Configuration;

public class ChatServer {
	private String brokerId;
	private String host;
	private String identityPort;
	private String replicaPort;
	private String heartbeatPort;
	private String topologyPort;
	private String masterConfigServer;
	private String logLocation;
	
	public ChatServer() {}
	
	public ChatServer(Configuration configuration) {
		this.brokerId = configuration.getBrokerId();
		this.host = configuration.getHost();
		this.identityPort = configuration.getIdentityPort();
		this.replicaPort = configuration.getReplicaPort();
		this.heartbeatPort = configuration.getHeartbeatPort();
		this.topologyPort = configuration.getTopologyPort();
		this.masterConfigServer = configuration.getMasterConfigServer();
		this.logLocation = configuration.getLogLocation();
	}
	
	public String getBrokerId() {
		return brokerId;
	}
	
	public String getHost() {
		return host;
	}

	public String getIdentityPort() {
		return identityPort;
	}

	public String getReplicaPort() {
		return replicaPort;
	}

	public String getHeartbeatPort() {
		return heartbeatPort;
	}

	public String getTopologyPort() {
		return topologyPort;
	}

	public String getMasterConfigServer() {
		return masterConfigServer;
	}

	public String getLogLocation() {
		return logLocation;
	}
}
