package com.notatracer.sandbox.app.websocket.web.http.interceptor;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Audit websocket creation.
 * @author grudkowm
 *
 */
public class MyHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	private Logger LOGGER = Logger.getLogger(MyHttpSessionHandshakeInterceptor.class);
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		LOGGER.info("MyHttpSessionHandshakeInterceptor::beforeHandshake");
		return super.beforeHandshake(request, response, wsHandler, attributes);
		
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		LOGGER.info("MyHttpSessionHandshakeInterceptor::afterHandshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
