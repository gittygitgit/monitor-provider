package com.notatracer.sandbox.app.websocket.integration.messaging;

public interface MessageParser<T> {

	void parse(T msg, MessageListener l);

}
