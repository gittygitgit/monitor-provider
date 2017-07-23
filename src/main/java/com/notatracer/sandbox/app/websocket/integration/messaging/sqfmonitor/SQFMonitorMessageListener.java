package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;

public interface SQFMonitorMessageListener extends MessageListener {

	public void onPortMaint(SeqSqfMonPortMaint msg);
	
	public void onPortActivity(SeqSqfMonPortActivity msg);
	
	public void onPortConnectionEvent(SeqSqfMonPortConnectionEvent msg);

}
