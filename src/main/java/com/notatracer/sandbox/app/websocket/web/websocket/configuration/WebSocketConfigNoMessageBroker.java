package com.notatracer.sandbox.app.websocket.web.websocket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.notatracer.sandbox.web.websocket.handler.MyHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfigNoMessageBroker implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(myHandler(), "/sqf") .addInterceptors(new HttpSessionHandshakeInterceptor()).setAllowedOrigins("http://localhost:8080").withSockJS();
	}
	
	@Bean
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}
	
	
	
}
