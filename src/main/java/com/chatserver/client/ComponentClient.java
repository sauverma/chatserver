package com.chatserver.client;

import java.util.concurrent.Callable;

public class ComponentClient implements Callable<String> {
	private String requestId;
	private String destIp;
	private String destPort;
	private String cmd;

	public ComponentClient(String requestId, String destIp, String destPort, String cmd) {
		this.requestId = requestId;
		this.destIp = destIp;
		this.destPort = destPort;
		this.cmd = cmd;
	}

	@Override
	public String call() throws Exception {
		return null;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getDestIp() {
		return destIp;
	}

	public String getDestPort() {
		return destPort;
	}

	public String getCmd() {
		return cmd;
	}
}
