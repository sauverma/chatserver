package com.chatserver.processing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.chatserver.configuration.Configuration;

/**
 * 
 * @author sauverma
 *
 *         This server exposes the configuration information of this chat
 *         server. Other chat servers get details about replica port, broker id,
 *         etc. using this server's data
 * 
 *         This also acts as a heartbeat emitter to the coordinator to indicate
 *         its up & running
 */

public class IdentityServer implements Runnable {
	private static final Logger logger = Logger.getLogger(IdentityServer.class);
	private ServerSocket serverSocket = null;
	private ExecutorService workers = Executors.newCachedThreadPool();
	private volatile boolean isStopped = false;
	
	@Override
	public void run() {
		logger.info("Starting IdentityServer @" + Configuration.getConfiguration().getIdentityPort());

		try {
			serverSocket = new ServerSocket(Integer.parseInt(Configuration.getConfiguration().getIdentityPort()));
		} catch (NumberFormatException e) {
			logger.error(e);
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot start IdentityServer at port " + Configuration.getConfiguration().getIdentityPort());
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot start IdentityServer at port " + Configuration.getConfiguration().getIdentityPort());
		}

		while (!isStopped) {
			Socket clientSocket = null;

			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				if (isStopped) {
					logger.error("IdentityServer Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			
			Future<String> identityResponse = workers.submit(new IdentityWorker(clientSocket));
		}
	}

}
