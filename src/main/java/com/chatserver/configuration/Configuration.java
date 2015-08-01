package com.chatserver.configuration;

public class Configuration {
	private String identityPort;
	private String replicaPort;
	private String heartbeatPort;
	private String topologyPort;
	private String brokerId;
	private String masterConfigServer;
	private String logLocation;
	
	private static Configuration configuration = new Configuration();
	
	private Configuration() {}
	
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getIdentityPort() {
		return identityPort;
	}

	public void setIdentityPort(String identityPort) {
		this.identityPort = identityPort;
	}

	public String getReplicaPort() {
		return replicaPort;
	}

	public void setReplicaPort(String replicaPort) {
		this.replicaPort = replicaPort;
	}

	public String getHeartbeatPort() {
		return heartbeatPort;
	}

	public void setHeartbeatPort(String heartbeatPort) {
		this.heartbeatPort = heartbeatPort;
	}
	
	public void setTopologyPort(String topologyPort) {
		this.topologyPort = topologyPort;
	}
	
	public String getTopologyPort() {
		return topologyPort;
	}
	
	public String getMasterConfigServer() {
		return masterConfigServer;
	}

	public void setMasterConfigServer(String masterConfigServer) {
		this.masterConfigServer = masterConfigServer;
	}
	
	public String getLogLocation() {
		return logLocation;
	}
	
	public void setLogLocation(String logLocation) {
		this.logLocation = logLocation;
	}

}
