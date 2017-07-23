package com.notatracer.sandbox.app.websocket.interceptor;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.notatracer.sandbox.app.websocket.Topics;
import com.notatracer.sandbox.app.websocket.integration.DropConnection;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SQFMonitorMessageListener;
import com.notatracer.sandbox.app.websocket.web.DefaultWebsocketSessionRepository;
import com.notatracer.sandbox.app.websocket.web.WebSocketContext;
import com.notatracer.sandbox.app.websocket.web.http.interceptor.MyHttpSessionHandshakeInterceptor;

/**
 * Listener interface for {@link SessionSubscribeEvent}. 
 * </p>
 * @author grudkowm
 *
 */
@Component
public class StompSubscribedEvent implements ApplicationListener<SessionSubscribeEvent> {

	private Logger LOGGER = Logger.getLogger(StompSubscribedEvent.class);

	@Autowired
	private SimpMessagingTemplate websocket;

	@Autowired
	private DropConnection sqfMonitorConnection;

	@Autowired
	private SQFMonitorMessageListener l;

	@Autowired
	private DefaultWebsocketSessionRepository websocketSessionRepository;

	public StompSubscribedEvent(SimpMessagingTemplate websocket) {
		super();
		this.websocket = websocket;
	}

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("StompSubscribedEvent::onApplicationEvent");
		}
		
		String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
		String dest = StompHeaderAccessor.wrap(event.getMessage()).getDestination();
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		HttpSession httpSession = (HttpSession)headerAccessor.getSessionAttributes().get(MyHttpSessionHandshakeInterceptor.SESSION_ATTR_SESSION);
				
		WebSocketContext webSocketContext = new WebSocketContext(httpSession, dest); 
		websocketSessionRepository.put(sessionId, webSocketContext);
		
		switch (dest) {
		case Topics.TOPIC_SQF:
			
			// connect to SQF Monitor app
			sqfMonitorConnection.connect(l);
			
			break;

		default:
			break;
		}
	}
}
