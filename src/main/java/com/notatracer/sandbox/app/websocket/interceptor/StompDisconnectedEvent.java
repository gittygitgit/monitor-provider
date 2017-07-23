package com.notatracer.sandbox.app.websocket.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.notatracer.sandbox.app.websocket.Topics;
import com.notatracer.sandbox.app.websocket.integration.DropConnection;
import com.notatracer.sandbox.app.websocket.web.WebSocketContext;
import com.notatracer.sandbox.app.websocket.web.WebsocketSessionRepository;

/**
 * Listener interface for {@link SessionDisconnectEvent}. 
 * </p>
 * @author grudkowm
 *
 */
@Component
public class StompDisconnectedEvent implements ApplicationListener<SessionDisconnectEvent> {

	private Logger LOGGER = Logger.getLogger(StompDisconnectedEvent.class);

	@Autowired
	private DropConnection sqfMonitorConnection;

	@Autowired
	private WebsocketSessionRepository websocketSessionRepository;

	public StompDisconnectedEvent() {
		super();
	}

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("SessionDisconnectEvent::onApplicationEvent");
		}
		
		WebSocketContext webSocketContext = websocketSessionRepository.get(event.getSessionId());

		switch (webSocketContext.getDestination()) {
		case Topics.TOPIC_SQF:
			
			// disconnect from SQF Monitor app
			sqfMonitorConnection.disconnect();

			break;

		default:
			break;
		}
	}
}
