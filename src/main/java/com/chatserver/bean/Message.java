package com.chatserver.bean;

import org.joda.time.DateTime;

public class Message {
	private final String producer;
	private final String consumer;
	private final String timestamp;
	private final String message;
	
	public Message() {
		this.producer = this.consumer = this.message = this.timestamp = null;
	}
	
	public Message (String producer, String consumer, String timestamp, String message) {
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
