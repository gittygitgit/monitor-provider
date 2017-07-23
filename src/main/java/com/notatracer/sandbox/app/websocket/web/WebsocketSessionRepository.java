package com.notatracer.sandbox.app.websocket.web;

public interface WebsocketSessionRepository {

	public void put(String sessionId, WebSocketContext ctx);
	
	public WebSocketContext get(String sessionId);
}
