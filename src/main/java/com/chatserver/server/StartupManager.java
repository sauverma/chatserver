package com.chatserver.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.chatserver.configuration.Configuration;

/**
 * 
 * @author sauverma
 *
 *         This class is responsible for :- 1. booting up from an inconsistent
 *         state via logs 2. starts up the services at different ports as
 *         mentioned in the Configuration
 */

public class StartupManager {
	private static final Logger logger = Logger.getLogger(StartupManager.class);
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private IdentityServer identityServer = null;
	private ReplicaServer replicaServer = null;
	private TopologyServer topologyServer = null;
	private static StartupManager startupManager = new StartupManager();

	private StartupManager() {
	}

	public static StartupManager getStartupManager() {
		return startupManager;
	}

	/**
	 * 
	 */
	public void startup() {

		/*
		 * ensure booting up from logs for consistency
		 */
		startupFromLog();
		identityServer = new IdentityServer("IdentityServer", Integer.parseInt(Configuration.getConfiguration().getIdentityPort()));
		replicaServer = new ReplicaServer("ReplicaServer", Integer.parseInt(Configuration.getConfiguration().getReplicaPort()));
		topologyServer = new TopologyServer("TopologyServer", Integer.parseInt(Configuration.getConfiguration().getTopologyPort()));
		
		executor.submit(identityServer);
		executor.submit(replicaServer);
		executor.submit(topologyServer);
	}

	/**
	 * 
	 */
	public void shutdown() {
		identityServer.shutdown();
		replicaServer.shutdown();
		topologyServer.shutdown();
	}

	/**
	 * 
	 */
	private void startupFromLog() {

	}
}
