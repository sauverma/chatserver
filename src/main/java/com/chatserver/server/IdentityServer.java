package com.chatserver.server;


/**
 * 
 * @author sauverma
 *
 *         This server exposes the configuration information of this chat
 *         server. Other chat servers get details about replica port, broker id,
 *         etc. using this server's data
 * 
 *         This also acts as a heartbeat emitter to the coordinator to indicate
 *         its up & running
 */

public class IdentityServer extends ComponentServer {
	public IdentityServer(String serverName, int serverPort) {
		super(serverName, serverPort, IdentityWorker.class);
	}
}
