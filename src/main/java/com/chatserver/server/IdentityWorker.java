package com.chatserver.server;

import java.net.Socket;

import org.apache.log4j.Logger;

import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IdentityWorker extends ComponentWorker {
	private static final Logger logger = Logger.getLogger(IdentityWorker.class);
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private enum COMMANDS {
		GETSTATUS("serverStatus"), GETCONFIG ("serverConfig");
		
		private String value;
		
		public String getValue() {
			return value;
		}
		
		COMMANDS (String value) {
			this.value = value;
		}
	};
	
	public IdentityWorker(String name, Socket clientSocket) {
		super(name, clientSocket);
	}
	
	@Override
	public String getResponseString(String cmd) {
		logger.debug(cmd);
		
		if (cmd.equals(COMMANDS.GETSTATUS.getValue())) {
			return "ok";
		}
		else if (cmd.equals(COMMANDS.GETCONFIG.getValue())) {
			try {
				return mapper.writeValueAsString(Configuration.getConfiguration());
			} catch (JsonProcessingException e) {
				logger.error(e);
				e.printStackTrace();
			}
			
			return "invalid config";
		}
		
		return "";
	}

}
