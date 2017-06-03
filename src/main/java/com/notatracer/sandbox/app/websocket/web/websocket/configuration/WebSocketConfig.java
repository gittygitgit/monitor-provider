package com.notatracer.sandbox.app.websocket.web.websocket.configuration;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import com.notatracer.sandbox.app.websocket.web.http.interceptor.MyHttpSessionHandshakeInterceptor;
import com.notatracer.sandbox.app.websocket.web.websocket.interceptor.InboundChannelInterceptor;
import com.notatracer.sandbox.app.websocket.web.websocket.interceptor.OutboundChannelInterceptor;

@Component
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	private Logger LOGGER = Logger.getLogger(WebSocketConfig.class);
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// ws clients connect to the application at this URL...
		StompWebSocketEndpointRegistration endpoint = registry.addEndpoint("/sqf");
		SockJsServiceRegistration registration = endpoint.withSockJS();
		registration.setInterceptors(new MyHttpSessionHandshakeInterceptor());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// messages prefixed with topic will be routed to the broker
		registry.enableSimpleBroker("/topic");	
		// messages prefixed with /app will be routed to @Controller methods
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		super.configureClientInboundChannel(registration);
		registration.setInterceptors(new InboundChannelInterceptor());
	}
	
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		super.configureClientOutboundChannel(registration);
		registration.setInterceptors(new OutboundChannelInterceptor());
	}
	
	
}
