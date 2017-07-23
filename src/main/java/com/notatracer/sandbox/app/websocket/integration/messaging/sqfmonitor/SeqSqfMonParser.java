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
		
		case PORT_MAINT:
			logger.debug("parse::PORT_MAINT");
			((SQFMonitorMessageListener)l).onPortMaint((SeqSqfMonPortMaint)msg);
			break;
		case PORT_ACTIVITY:
			logger.debug("parse::PORT_ACTIVITY");
			((SQFMonitorMessageListener)l).onPortActivity((SeqSqfMonPortActivity)msg);
			break;
		case PORT_CONNECTION_EVENT:
			logger.debug("parse::PORT_CONNECTION_EVENT");
			((SQFMonitorMessageListener)l).onPortConnectionEvent((SeqSqfMonPortConnectionEvent)msg);
			break;
		default:
			break;
		}
		
		
	}

}
