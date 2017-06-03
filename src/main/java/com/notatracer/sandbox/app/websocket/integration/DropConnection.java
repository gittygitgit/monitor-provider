package com.notatracer.sandbox.app.websocket.integration;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;

/**
 * Represents a connection to a data drop application.
 * @author grudkowm
 *
 */
public interface DropConnection {
	public void connect(MessageListener l);
	public void connect(MessageListener l, String url);
	public void disconnect();
	public void onData(Object o);
}
