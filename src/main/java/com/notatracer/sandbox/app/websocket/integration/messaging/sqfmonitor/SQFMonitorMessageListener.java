package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;

public interface SQFMonitorMessageListener extends MessageListener {

	public void onFirmUpdate(SeqSqfMonFirmUpdate msg);
	
}
