package com.chatserver.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sauverma
 *
 * A message queue for each user, like a mailbox, all messages
 * sent to a user will be stored in that user's MessageQueue
 */

public class MessageQueue {
	//private static final Logger logger = Logger.getLogger(MessageQueue.class);
	
	private final List<Message> messageList;
	private long offset;
	
	public MessageQueue () {
		messageList = new ArrayList<Message>();
		offset = 0;
	}
	
	public synchronized void storeMessage (Message message) {
		messageList.add(message);
	}
	
	public synchronized long getOffset() {
		return offset;
	}
	
	public synchronized List<Message> getMessages (long from, long to) {
		if (from <= to) {
			return messageList.subList((int) from, (int) to);
		}
	
		return new ArrayList<Message>();
	}
	
	public synchronized boolean saveMessages (List<Message> messages) {
		return messageList.addAll(messages);
	}
	
	public synchronized boolean saveMessage (Message message) {
		return messageList.add(message);
	}
	
	public synchronized List<Message> readMessage() {
		List<Message> msgs = new ArrayList<Message>();
		
		for (long i = offset; i < messageList.size(); i++) {
			msgs.add(messageList.get((int) i));
		}
		
		return msgs;
	}
	
	private synchronized void markRead (long offsetIncrement) {
		if (offset + offsetIncrement < messageList.size()) {
			System.out.println("Error : cant increment by offsetIncrement");
		}
		
		offset += offsetIncrement;
	}
}
