package com.chatserver.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * 
 * @author sauverma
 *
 *         General purpose server opening serverSocket at specified port and
 *         start serving
 */

public abstract class ComponentServer implements Runnable {
	private static final Logger logger = Logger.getLogger(ComponentServer.class);
	private ServerSocket serverSocket = null;
	private ExecutorService workers = Executors.newCachedThreadPool();
	private volatile boolean isStopped = false;
	private Class<? extends ComponentWorker> componentWorkerClass = null;

	private String serverName;
	private int serverPort;

	public ComponentServer(String serverName, int serverPort, Class<? extends ComponentWorker> componentWorkerClass) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.componentWorkerClass = componentWorkerClass;
	}

	@Override
	public void run() {
		logger.info("Starting " + serverName + " @" + serverPort);

		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (NumberFormatException e) {
			logger.error(e);
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot start " + serverName + " at port " + serverPort);
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot start " + serverName + " at port " + serverPort);
		}

		while (!isStopped) {
			Socket clientSocket = null;

			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				if (isStopped) {
					logger.error(serverName + " Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}

			try {
				Future<String> response = workers.submit(componentWorkerClass.getConstructor(String.class, Socket.class).newInstance(new Object[] { serverName, clientSocket }));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public void shutdown() {
		logger.info("Shutting down " + serverName + " @" + serverPort);
		isStopped = true;
	}
}
