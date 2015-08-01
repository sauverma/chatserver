package com.chatserver.server;

import java.net.Socket;

public class TopologyWorker extends ComponentWorker {

	public TopologyWorker(String name, Socket clientSocket) {
		super(name, clientSocket);
	}

	@Override
	public String getResponseString(String cmd) {
		return null;
	}

}
