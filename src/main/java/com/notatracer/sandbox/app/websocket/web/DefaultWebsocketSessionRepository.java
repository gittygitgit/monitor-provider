package com.notatracer.sandbox.app.websocket.web;

import java.util.HashMap;
import java.util.Map; 

import org.springframework.stereotype.Component;

@Component 
public class DefaultWebsocketSessionRepository implements WebsocketSessionRepository {

	private Map<String, WebSocketContext> repository = new HashMap<>();
	
	public DefaultWebsocketSessionRepository() {
		super();
		System.out.println("WebsocketSessionRepository");
	}

	@Override
	public void put(String sessionId, WebSocketContext ctx) {
		this.repository.put(sessionId, ctx);
	}

	@Override
	public WebSocketContext get(String sessionId) {
		return this.repository.get(sessionId);
	}

}
