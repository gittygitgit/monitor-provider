package com.notatracer.sandbox.app.websocket.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebsocketSessionRepositoryConfig {

	@Bean
	public DefaultWebsocketSessionRepository websocketSessionRepository() {
		return new DefaultWebsocketSessionRepository();
	}
}
