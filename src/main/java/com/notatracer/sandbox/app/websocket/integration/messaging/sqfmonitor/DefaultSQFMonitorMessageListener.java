package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notatracer.sandbox.app.websocket.Topics;
import com.notatracer.sandbox.app.websocket.domain.PortActivity;
import com.notatracer.sandbox.app.websocket.domain.PortConnectionEvent;
import com.notatracer.sandbox.app.websocket.domain.PortMaint;
import com.notatracer.sandbox.app.websocket.domain.SQFMonDomainObject;
import com.notatracer.sandbox.app.websocket.integration.messaging.Message.ConnectionEvent;

/**
 * Notified of messages from the SQF monitor ring application.
 * </p>
 * Converts from inet format into domain and publishes to websocket topic.
 * 
 * @author grudkowm
 */
@Component
public class DefaultSQFMonitorMessageListener implements SQFMonitorMessageListener {

	@Autowired
	private SimpMessagingTemplate websocket;

	@Override
	public void onPortActivity(SeqSqfMonPortActivity msg) {
		PortActivity pa = new PortActivity();

		pa.last = new String(msg.last);
		pa.msgType = (char)msg.msgType.getVal();
		pa.groupName = new String(msg.groupName);
		pa.name = new String(msg.name);
		pa.ringName = new String(msg.ringName);
		pa.blocks = msg.blocks;
		pa.numBlocks = msg.numBlocks;
		pa.numPurges = msg.numPurges;
		pa.numQuotes = msg.numQuotes;
		pa.numUndPurges = msg.numUndPurges;
		
		pa.limit1Min = new BigDecimal(msg.limit1Min).movePointLeft(4);
		pa.limit5Min = new BigDecimal(msg.limit5Min).movePointLeft(4);
		pa.limitCurrent = new BigDecimal(msg.limitCurrent).movePointLeft(4);

		pa.rate1Min = msg.rate1Min;
		pa.rate5Min = msg.rate5Min;
		pa.rateCurrent = msg.rateCurrent;
		
		pa.ql1Min = new BigDecimal(msg.ql1Min).movePointLeft(4);
		pa.qlCurrent = new BigDecimal(msg.qlCurrent).movePointLeft(4);
		
		convertAndSend(pa);
	}

	private void convertAndSend(SQFMonDomainObject o) {
		try {
			this.websocket.convertAndSend(Topics.TOPIC_SQF,
					new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(o));
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPortConnectionEvent(SeqSqfMonPortConnectionEvent msg) {
		PortConnectionEvent pce = new PortConnectionEvent();
		pce.msgType = (char)msg.msgType.getVal();
		pce.groupName = new String(msg.groupName);
		pce.portName = new String(msg.portName);
		pce.ringName = new String(msg.ringName);
		pce.event = ConnectionEvent.forValue(msg.event).toString();
		pce.time = new String(msg.time);
		convertAndSend(pce);
	}

	@Override
	public void onPortMaint(SeqSqfMonPortMaint msg) {
		PortMaint pm = new PortMaint(msg.id, new String(msg.groupName), new String(msg.name), new String(msg.ringName));
		pm.msgType = (char)msg.msgType.getVal();
		convertAndSend(pm);
	}

}
