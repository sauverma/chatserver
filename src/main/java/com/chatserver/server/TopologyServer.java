package com.chatserver.server;

/**
 * 
 * @author sauverma
 *
 * This class spawns the Topology knowledge threads,
 * these are responsible for knowing which chatServers are
 * my siblings, which are alive/dead
 * 
 * The ChatServers will create the topology amongst themselves
 * For ex. if neighbour(cs1) = {cs2,cs3} and cs4 is added to cs1
 * as a neighbour, then while this addition, cs4 will get info
 * about the neighour(cs1) and similarly cs1 will get info about
 * neighbour(cs4), then topology updates would happen and finally
 * all the CS will know about each other 
 * 
 */

public class TopologyServer extends ComponentServer {
	public TopologyServer(String serverName, int serverPort) {
		super(serverName, serverPort, TopologyWorker.class);
	}
}
