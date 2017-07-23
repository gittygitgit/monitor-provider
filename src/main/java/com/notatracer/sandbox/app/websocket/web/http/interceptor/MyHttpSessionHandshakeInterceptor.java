package com.notatracer.sandbox.app.websocket.web.http.interceptor;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.notatracer.sandbox.app.websocket.web.WebSocketContext;
import com.notatracer.sandbox.app.websocket.web.WebsocketSessionRepository;

/**
 * Audit websocket creation.
 * @author grudkowm
 *
 */
@Component
public class MyHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	public static final String SESSION_ATTR_SESSION_ID = "sessionId";
	public static final String SESSION_ATTR_SESSION = "session";


	private Logger LOGGER = Logger.getLogger(MyHttpSessionHandshakeInterceptor.class);

	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("MyHttpSessionHandshakeInterceptor::beforeHandshake");
		}

		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession();
			attributes.put(SESSION_ATTR_SESSION_ID, session.getId());
			attributes.put(SESSION_ATTR_SESSION, session);
			
		}
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("MyHttpSessionHandshakeInterceptor::afterHandshake");
		}

		super.afterHandshake(request, response, wsHandler, ex);
	}
}
