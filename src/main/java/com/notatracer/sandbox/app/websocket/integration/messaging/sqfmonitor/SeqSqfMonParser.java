package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.MessageParser;

@Component 
public class SeqSqfMonParser implements MessageParser<SeqSqfMonMsg> {

	private Logger logger = Logger.getLogger(SeqSqfMonParser.class);
	
	@Override
	public void parse(SeqSqfMonMsg msg, MessageListener l) {
		
		if (!SQFMonitorMessageListener.class.isAssignableFrom(l.getClass())) {
			throw new IllegalArgumentException("Unsupported listener class [class=" + l.getClass().getSimpleName() + ", required=SQFMonitorMessageListener]");
		}
		switch (msg.msgType) {
		case FIRM_UPDATE:
			logger.debug("parse::firm update");
			((SQFMonitorMessageListener)l).onFirmUpdate((SeqSqfMonFirmUpdate)msg);
			break;

		default:
			break;
		}
		
		
	}

}
