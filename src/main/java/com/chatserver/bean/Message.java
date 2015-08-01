package com.chatserver.bean;

import org.joda.time.DateTime;

public class Message {
	private final String producer;
	private final String consumer;
	private final DateTime timestamp;
	private final String message;
	
	public Message (String producer, String consumer, DateTime timestamp, String message) {
		this.producer = producer;
		this.consumer = consumer;
		this.timestamp = timestamp;
		this.message = message;
	}
	
	public String getProducer() {
		return producer;
	}

	public String getConsumer() {
		return consumer;
	}

	public String getTimestamp() {
		return timestamp.toString();
	}
	
	public String getMessage() {
		return message;
	}
}
