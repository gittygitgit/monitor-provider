package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notatracer.sandbox.app.websocket.Topics;
import com.notatracer.sandbox.app.websocket.domain.FirmSummary;

/**
 * Notified of messages from the SQF monitor ring application.
 * </p>
 * Converts from inet format into domain and publishes to websocket topic.
 * 
 * @author grudkowm
 *
 */
@Component
public class DefaultSQFMonitorMessageListener implements SQFMonitorMessageListener {


	@Autowired
	private SimpMessagingTemplate websocket;
	
	@Override
	public void onFirmUpdate(SeqSqfMonFirmUpdate msg) {

		FirmSummary fs = new FirmSummary();
		
		fs.latency = new BigDecimal(msg.latency);
		fs.name = new String(msg.firmName);
		fs.numBlocks = msg.numBlocks;
		fs.numPurges = msg.numPurges;
		fs.numQuotes = msg.numQuotes;
		fs.numUndPurges = msg.numUndPurges;
		fs.portName = new String(msg.portName);
		try {
			this.websocket.convertAndSend(Topics.TOPIC_SQF, new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(fs));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
