package com.notatracer.sandbox.app.websocket.web;

import javax.servlet.http.HttpSession;

public class WebSocketContext {

	private HttpSession session;
	private String destination;
 
	public WebSocketContext() {
		super();
	}
	
	public WebSocketContext(HttpSession session) {
		super();
		this.session = session;
	}

	public WebSocketContext(HttpSession session, String destination) {
		super();
		this.session = session;
		this.destination = destination;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getDestination() {
		return destination;
	}
	
	public String getSessionId() {
		return session.getId();
	}

	public void setDestination(String dest) {
		this.destination = dest;
	}

}
