package com.chatserver.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	private static final Logger logger = Logger.getLogger(Utils.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Socket getSocketConnection(String destIp, String destPort) {
		Socket socket = null;
		try {
			socket = new Socket(destIp, Integer.parseInt(destPort));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return socket;
	}
	
	public static String getMessageEndTerminal() {
		return "%^%";
	}
	
	public static String getActionableLogHeader() {
		return " ACTIONABLE ";
	}
	
	public static ObjectMapper getObjectMapper() {
		return mapper;
	}
}