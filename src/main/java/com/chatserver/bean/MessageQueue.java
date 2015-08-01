package com.chatserver.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author sauverma
 *
 * A message queue for each user, like a mailbox, all messages
 * sent to a user will be stored in that user's MessageQueue
 */

public class MessageQueue {
	private static final Logger logger = Logger.getLogger(MessageQueue.class);
	
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
	
	public synchronized Map<Long, List<Message>> readMessage() {
		List<Message> msgs = new ArrayList<Message>();
		
		long i = offset;
		
		for (; i < messageList.size(); i++) {
			msgs.add(messageList.get((int) i));
		}
		
		Map<Long, List<Message>> retMap = new HashMap<Long, List<Message>>();
		retMap.put(i, msgs);
		return retMap;
	}
	
	public synchronized long markRead (long offset) {
		if (offset > messageList.size()) {
			logger.error("Invalid offset passed in markRead");
		}
		
		this.offset = offset;
		return this.offset;
	}
}
