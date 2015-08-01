package com.chatserver.server;

/**
 * 
 * @author sauverma
 *
 * This class spawns the Topology knowledge threads,
 * these are responsible for knowing which chatServers are
 * my siblings, which are up/down
 * 
 * The MasterConfigServer will keep on updating the 
 * ChatServers via these TopologyServer threads, if the MCS
 * goes down, then the ChatServers will work with a cache of
 * topology considering it to be the true one
 */

public class TopologyServer extends ComponentServer {
	public TopologyServer(String serverName, int serverPort) {
		super(serverName, serverPort, TopologyWorker.class);
	}
}
