package com.notatracer.sandbox.app.websocket.interceptor;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notatracer.sandbox.app.websocket.Topics;
import com.notatracer.sandbox.app.websocket.data.SQFMonitorRepository;
import com.notatracer.sandbox.app.websocket.domain.FirmSummary;
import com.notatracer.sandbox.app.websocket.integration.DropConnection;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SQFMonitorMessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonFirmUpdate;

/**
 * Listener interface for {@link SessionSubscribeEvent}. 
 * </p>
 * @author grudkowm
 *
 */
@Component
public class StompSubscribedEvent implements ApplicationListener<SessionSubscribeEvent>, SQFMonitorMessageListener {

	private Logger LOGGER = Logger.getLogger(StompSubscribedEvent.class);

	@Autowired
	private SimpMessagingTemplate websocket;
	
	@Autowired
	private SQFMonitorRepository sqfMonitorRepo;
	
	@Autowired
	private DropConnection sqfMonitorConnection;
	
	@Autowired
	private SQFMonitorMessageListener l;
	
	public StompSubscribedEvent(SimpMessagingTemplate websocket, SQFMonitorRepository sqfMonitorRepo) {
		super();
		this.websocket = websocket;
		this.sqfMonitorRepo = sqfMonitorRepo;
	}

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		LOGGER.info("StompSubscribedEvent::onApplicationEvent");
		LOGGER.info(event);	
		
		Map<String, List<Object>> multiValueMap = (Map<String, List<Object>>)event.getMessage().getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS);
		String dest = (String)((List<Object>)multiValueMap.get(StompHeaderAccessor.STOMP_DESTINATION_HEADER)).get(0);

		switch (dest) {
		case Topics.TOPIC_SQF:
			
			// connect to SQF Monitor app
			sqfMonitorConnection.connect(l);
			
//			// send initial snapshot
//			getInitialSnapshot();
//			
			
			// After initial snapshot, updates will be streamed to clients
			// as they are published by the data provider.
			
			// TODO: Coordinating between an initial snapshot and subsequent real-time 
			// TODO: updates.
			break;

		default:
			break;
		}
	}

	private void getInitialSnapshot() {
		// check repository for current snapshot
		Map<String, FirmSummary> currentSnapshot = sqfMonitorRepo.getCurrentSnapshot();
		
		try {
			this.websocket.convertAndSend(Topics.TOPIC_SQF, new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(currentSnapshot));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onFirmUpdate(SeqSqfMonFirmUpdate msg) {
		// TODO Auto-generated method stub
		
	}

}
