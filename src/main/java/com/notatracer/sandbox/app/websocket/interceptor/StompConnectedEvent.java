package com.notatracer.sandbox.app.websocket.interceptor;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class StompConnectedEvent implements ApplicationListener<SessionConnectedEvent> {

	private Logger LOGGER = Logger.getLogger(StompConnectedEvent.class);

	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		if (LOGGER.isTraceEnabled()) {
	 		LOGGER.info("StompConnectedEvent::onApplicationEvent");
			LOGGER.info(event);
		}
	}

}
