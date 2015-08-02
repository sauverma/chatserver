package com.chatserver.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.chatserver.utils.Utils;

public abstract class ComponentWorker implements Callable<String> {
	private static final Logger logger = Logger.getLogger(ComponentWorker.class);
	
	private String name;
	protected Socket clientSocket;
	
	public ComponentWorker(String name, Socket clientSocket) {
		this.name = name;
		this.clientSocket = clientSocket;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract String getResponseString(String cmd);
	
	@Override
	public String call() throws Exception {
		logger.debug("Request from : " + clientSocket.getRemoteSocketAddress());
		
		String response = "";
		
		InputStream input = clientSocket.getInputStream();
		OutputStream output = clientSocket.getOutputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String msg = "";
		String cmd = "";
		
		while ((cmd = br.readLine()) != null) {
			if (cmd.equals(Utils.getMessageEndTerminal()))
				break;
			
			msg += cmd;
			logger.debug(cmd);
		}
		
		logger.debug("CMD : " + msg);
		String reply = getResponseString(msg);

		logger.debug("REPLY : " + reply);
		
		PrintWriter pw = new PrintWriter(output);
		pw.println(reply);
		pw.println(Utils.getMessageEndTerminal());
		pw.flush();
		
		br.close();
		pw.close();
		input.close();
		output.close();
		
		return cmd;
	}
}
