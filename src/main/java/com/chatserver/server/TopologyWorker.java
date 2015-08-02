package com.chatserver.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.chatserver.bean.ChatServer;
import com.chatserver.bean.Topology;
import com.chatserver.configuration.Configuration;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopologyWorker extends ComponentWorker {

	private static final Logger logger = Logger.getLogger(TopologyWorker.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private enum COMMANDS {
		SHARETOPOLOGY("shareTopology");

		private String value = null;

		public String getValue() {
			return value;
		}

		COMMANDS(String value) {
			this.value = value;
		}
	};

	public TopologyWorker(String name, Socket clientSocket) {
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
		} else
			msg = cmd;

		logger.debug(msg + " : " + payload);

		/*
		 * handle the COPY command from sibling chatServers
		 */
		if (msg.equals(COMMANDS.SHARETOPOLOGY.getValue())) {
			logger.debug(payload);

			// assumption : payload is a valid json object
			// convert this json to List<ChatServer> and apply on the existing
			// Topology
			
			try {
				List<ChatServer> chatServers = mapper.readValue(payload.getBytes(), new TypeReference<List<ChatServer>>() {});
				logger.debug(mapper.writeValueAsString(chatServers));
				
				Collection<ChatServer> servers = new ArrayList<ChatServer>();
				servers.addAll(Topology.getTopology().getChatServers());
				servers.add(new ChatServer(Configuration.getConfiguration()));
				
				for (ChatServer neighbourServer : chatServers) {
					if (Topology.getTopology().hasChatServer(neighbourServer) == false && !neighbourServer.getBrokerId().equals(Configuration.getConfiguration().getBrokerId())) {
						Topology.getTopology().addChatServer(neighbourServer);
					}
				}

				return "success-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + ":" + Configuration.getConfiguration().getTopologyPort() + "^" + mapper.writeValueAsString(servers);
			} catch (JsonParseException e) {
				e.printStackTrace();
				return "failed-" + updateId + "-" + Configuration.getConfiguration().getBrokerId() + ":" + Configuration.getConfiguration().getTopologyPort();
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return "failed-" + updateId;
			} catch (IOException e) {
				e.printStackTrace();
				return "failed-" + updateId;
			}
		}

		return "";
	}
}
