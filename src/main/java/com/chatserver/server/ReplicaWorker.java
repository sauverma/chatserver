package com.chatserver.server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chatserver.bean.Message;
import com.chatserver.bean.MessageStore;
import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReplicaWorker extends ComponentWorker {
	
	private static final Logger logger = Logger.getLogger(ReplicaWorker.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private enum COMMANDS {
		COPYMESSAGES("copy"), UPDATEOFFSETS("updateOffsets");
		
		private String value = null;
		
		public String getValue() {
			return value;
		}
		
		COMMANDS(String value) {
			this.value = value;
		}
	};
	
	public ReplicaWorker(String name, Socket clientSocket) {
		super(name, clientSocket);
	}
	
	@Override
	public String getResponseString(String cmd) {
		/*
		 * cmd = command-updateId-json_payload
		 */
		
		logger.debug(cmd);
		
		String msg = ""; 
		String updateId = "";
		String payload = "";
		
		if (cmd.indexOf("-") != -1) {
			msg = cmd.substring(0, cmd.indexOf("-"));
			cmd = cmd.substring(cmd.indexOf("-") + 1, cmd.length());
			updateId = cmd.substring(0, cmd.indexOf("-"));
			payload = cmd.substring(cmd.indexOf("-") + 1, cmd.length());
		}
		else
			msg = cmd;
		
		logger.debug(msg + " : " + payload);
		
		/*
		 * handle the COPY command from sibling chatServers
		 */
		if (msg.equals(COMMANDS.COPYMESSAGES.getValue())) {
			logger.debug(payload);
			
			// assumption : payload is a valid json object
			// convert this json to List<Message> and apply on the existing MessageStore
			try {
				Map<String, List<Message>> messages = mapper.readValue(payload.getBytes(), new TypeReference<Map<String, List<Message>>>() {});
				logger.debug(mapper.writeValueAsString(messages));

				for (String consumer : messages.keySet()) {
					for (Message message : messages.get(consumer)) {
						MessageStore.getMessageStore().saveMessages(message.getProducer(), message.getConsumer(), message.getTimestamp(), message.getMessage());
					}
				}
				
				return "success-" + updateId + "-" + messages.size();
			} catch (JsonParseException e) {
				e.printStackTrace();
				return "failed-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + "-" + Configuration.getConfiguration().getReplicaPort();
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return "failed-" + updateId;
			} catch (IOException e) {
				e.printStackTrace();
				return "failed-" + updateId;
			}
		}
		/*
		 * handle the UPDATEOFFSETS command from sibling chatServers
		 * this is triggered when a consumer reads messages from 
		 * some other chatServer, we need to update the last read value
		 * to this chatServer as well
		 */
		else if (msg.equals(COMMANDS.UPDATEOFFSETS.getValue())) {
			logger.debug(payload);
			
			try {
				Map<String, Long> offsetsPerConsumer = mapper.readValue(payload.getBytes(), new TypeReference<Map<String, Long>>(){});
				
				for (String consumer : offsetsPerConsumer.keySet()) {
					MessageStore.getMessageStore().markRead(consumer, offsetsPerConsumer.get(consumer));
				}
				
				return "success-" + updateId;
			} catch (JsonParseException e) {
				e.printStackTrace();
				return "failed-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + "-" + Configuration.getConfiguration().getReplicaPort();
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return "failed-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + "-" + Configuration.getConfiguration().getReplicaPort();
			} catch (IOException e) {
				e.printStackTrace();
				return "failed-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + "-" + Configuration.getConfiguration().getReplicaPort();
			}
		}

		return "";
	}
}
