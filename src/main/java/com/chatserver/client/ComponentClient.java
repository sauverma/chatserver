package com.chatserver.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.chatserver.utils.Utils;

public class ComponentClient implements Callable<String> {

	private static final Logger logger = Logger.getLogger(ComponentClient.class);

	private String requestId;
	private String cmd;
	private String payload;
	private Socket socket;

	public ComponentClient(String requestId, Socket socket, String cmd, String payload) {
		this.requestId = requestId;
		this.cmd = cmd;
		this.socket = socket;
		this.payload = payload;
	}

	@Override
	public String call() throws Exception {
		logger.debug("ComponentClient socket to " + socket.getRemoteSocketAddress() + " for cmd : " + cmd + " having payload : " + payload + " with reqId : " + requestId);

		InputStream input = socket.getInputStream();
		OutputStream output = socket.getOutputStream();

		PrintWriter pw = new PrintWriter(output);

		pw.println(cmd + "-" + requestId + "-" + payload);
		pw.println(Utils.getMessageEndTerminal());
		pw.flush();
		
		String response = "";
		String line = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		while ((line = br.readLine()) != null) {
			if (line.equals(Utils.getMessageEndTerminal()))
				break;
			
			response += line;
		}
		
		br.close();
		pw.close();
		input.close();
		output.close();
	
		return response;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public String getCmd() {
		return cmd;
	}
}
