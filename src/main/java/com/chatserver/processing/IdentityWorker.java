package com.chatserver.processing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class IdentityWorker implements Callable<String> {
	private enum COMMANDS {
		GETSTATUS("status"), GETCONFIG ("config");
		
		private String value;
		
		public String getValue() {
			return value;
		}
		
		COMMANDS (String value) {
			this.value = value;
		}
	};
	
	private static final Logger logger = Logger.getLogger(IdentityWorker.class);
	private Socket clientSocket = null;
	
	public IdentityWorker(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public String call() throws Exception {
		String response = "";
		
		InputStream input = clientSocket.getInputStream();
		OutputStream output = clientSocket.getOutputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String cmd = br.readLine();
		
		logger.info(cmd);
		
		return cmd;
	}

}
