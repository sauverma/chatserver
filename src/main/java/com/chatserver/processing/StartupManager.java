package com.chatserver.processing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * 
 * @author sauverma
 *
 * This class is responsible for :-
 * 1. booting up from an inconsistent state via logs
 * 2. starts up the services at different ports as mentioned in the Configuration
 */

public class StartupManager {
	private static final Logger logger = Logger.getLogger(StartupManager.class);
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private static StartupManager startupManager = new StartupManager();
	
	private StartupManager() {}
	
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
		executor.submit(new IdentityServer());
		
	}
	
	/**
	 * 
	 */
	private void startupFromLog() {
		
	}
}
