package com.notatracer.sandbox.app.websocket.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQFMessageStream implements CommandLineRunner {

	private final SimpMessagingTemplate websocket;
	private Logger LOGGER = Logger.getLogger(SQFMessageStream.class);
	
	@Autowired
	public SQFMessageStream(SimpMessagingTemplate websocket) {
		this.websocket = websocket;
	}

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("MessageGenerator::run");
		LOGGER.info("MessageGenerator::run");
		
		for (int i = 0; i < 10; i++) {
			this.websocket.convertAndSend(
					"/topic/sqf", "test" + i);
		}
	}

}
