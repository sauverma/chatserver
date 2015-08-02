package com.chatserver.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class ComponentClient implements Callable<String> {

	private static final Logger logger = Logger.getLogger(ComponentClient.class);

	private String requestId;
	private String destIp;
	private String destPort;
	private String cmd;
	private String payload;

	public ComponentClient(String requestId, String destIp, String destPort, String cmd, String payload) {
		this.requestId = requestId;
		this.destIp = destIp;
		this.destPort = destPort;
		this.cmd = cmd;
		this.payload = payload;
	}

	@Override
	public String call() throws Exception {
		logger.debug("ComponentClient socket to " + destIp + ":" + destPort + " for cmd : " + cmd + " having payload : " + payload + " with reqId : " + requestId);

		Socket socket = new Socket(destIp, Integer.parseInt(destPort));

		InputStream input = socket.getInputStream();
		OutputStream output = socket.getOutputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		PrintWriter pw = new PrintWriter(output);

		pw.write(cmd + "-" + requestId + "-" + payload);
		pw.flush();
		pw.close();
		input.close();

		String response = "";
		String line = "";

		while ((line = br.readLine()) != null) {
			response += line;
		}

		br.close();
		output.close();
		socket.close();

		return response;
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
