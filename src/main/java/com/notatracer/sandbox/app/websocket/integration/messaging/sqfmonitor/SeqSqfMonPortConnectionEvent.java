package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.util.Arrays;

public class SeqSqfMonPortConnectionEvent extends SeqSqfMonMsg {
	
	public byte[] time = new byte[12];
	public byte[] portName = new byte[6];
	public byte[] groupName = new byte[6];
	public byte[] ringName = new byte[4];
	public byte event;

	public SeqSqfMonPortConnectionEvent() {
		super();
		msgType = MsgType.PORT_CONNECTION_EVENT;
		this.clear();
	}

	@Override
	public void clear() {
		Arrays.fill(this.portName, (byte) ' ');
		Arrays.fill(this.groupName, (byte) ' ');
		Arrays.fill(this.ringName, (byte) ' ');
		this.event = 0;
	}
}
