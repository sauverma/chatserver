package com.chatserver.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chatserver.client.ReplicaClientManager;

/**
 * 
 * @author sauverma
 * 
 *         This class is central to the chat server, all the messages received
 *         by this chat server for a consumer are store in MessageStore, its a
 *         singleton class
 *
 */

public class MessageStore {
	private static Object myLock = new Object();

	/*
	 * data structure containing the mapping from User id to User message queue
	 * at this chat server
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

	public Map<Long, List<Message>> getMessages(String consumer) {
		if (messages.get(consumer) == null)
			return new HashMap<Long, List<Message>>();

		return messages.get(consumer).readMessage();
	}

	public long markRead(String consumer, long offset) {
		if (messages.get(consumer) != null) {
			return messages.get(consumer).markRead(offset);
		}

		return -1;
	}

	public boolean saveMessages(String producer, String consumer, String timestamp, String message) {

		if (messages.get(consumer) == null) {
			synchronized (myLock) {
				if (messages.get(consumer) == null) {
					messages.put(consumer, new MessageQueue());
				}
			}
		}

		/*
		 * save the message locally and then transmit it for getting replicated
		 * on the sibling chatServers
		 */
		Message msg = new Message(producer, consumer, timestamp.toString(), message);
		messages.get(consumer).saveMessage(msg);
		ReplicaClientManager.getReplicaClientManager().broadcastReplicaCopyRequest(Topology.getTopology().getChatServers(), msg);

		return true;
	}
}
