package com.chatserver.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

/**
 * 
 * @author sauverma
 * 
 * This class is central to the chat server, all the messages received by this 
 * chat server for a consumer are store in MessageStore, its a singleton class
 *
 */

public class MessageStore {
	private static Object myLock = new Object();
	
	/*
	 * data structure containing the mapping from User id to User message queue at this chat server
	 */
	private ConcurrentHashMap<String, MessageQueue> messages;
	
	private static MessageStore messageStore;
	
	private MessageStore() {
		messages = new ConcurrentHashMap<String, MessageQueue>();
	}
	
	public static MessageStore getMessageStore() {
		if (messageStore == null) {
			synchronized (myLock) {
				if (messageStore == null) {
					System.out.println("Instantiating MessageStore");
					messageStore = new MessageStore();
				}
			}
		}
		
		return messageStore;
	}
	
	public List<Message> getMessages (String consumer) {
		if (messages.get(consumer) == null) 
			return new ArrayList<Message>();

		return messages.get(consumer).readMessage();
	}
	
	public boolean saveMessages (String producer, String consumer, DateTime timestamp, String message) {
		
		if (messages.get(consumer) == null) {
			synchronized (myLock) {
				if (messages.get(consumer) == null) {
					messages.put(consumer, new MessageQueue());
				}
			}
		}
		
		messages.get(consumer).saveMessage(new Message(producer, consumer, timestamp, message));
		
		return true;
	}
}
