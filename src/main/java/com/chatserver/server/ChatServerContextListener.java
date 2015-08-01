package com.chatserver.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatServerContextListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(ChatServerContextListener.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("ChatServerContextListener destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("ChatServerContextListener init");

		ServletContext sc = sce.getServletContext();
		Configuration config = Configuration.getConfiguration();

		config.setHost(sc.getInitParameter("host"));
		config.setIdentityPort(sc.getInitParameter("identityPort"));
		config.setBrokerId(sc.getInitParameter("brokerId"));
		config.setHeartbeatPort(sc.getInitParameter("heartbeatPort"));
		config.setReplicaPort(sc.getInitParameter("replicaPort"));
		config.setMasterConfigServer(sc.getInitParameter("coordinatorIp"));
		config.setLogLocation(sc.getInitParameter("logLocation"));
		config.setTopologyPort(sc.getInitParameter("topologyPort"));

		try {
			logger.info("Configuration : " + mapper.writeValueAsString(config));
		} catch (JsonProcessingException e) {
			logger.info("Invalid configuration");
			throw new IllegalArgumentException("Invalid configuration");
		}
		
		/*
		 * call the startupManager to start the server & its components
		 */
		StartupManager.getStartupManager().startup();
	}
}
