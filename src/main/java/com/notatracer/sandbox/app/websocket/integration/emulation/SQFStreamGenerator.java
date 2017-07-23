package com.notatracer.sandbox.app.websocket.integration.emulation;

import java.util.ArrayList;
import java.util.List;

import com.notatracer.sandbox.app.websocket.integration.messaging.Message;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SQFMonitorMessageListener;

/**
 * Generates a stream of messages emulating a connection to SQFFairy.
 * @author grudkowm
 *
 */
public class SQFStreamGenerator implements StreamGenerator {

	private SQFMonitorMessageListener listener;

	public SQFStreamGenerator(SQFMonitorMessageListener l) {
		this.listener = l;
	}

	@Override
	public void start() {
		loadScript();
		
	}

	private void loadScript() {
		List<String> script = new ArrayList();
//		script.add(Message.MsgType.PORT_MAINT + "|" + 
		
	}
	
	
}
