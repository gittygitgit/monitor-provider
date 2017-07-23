package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.util.Arrays;

public class SeqSqfMonPortMaint  extends SeqSqfMonMsg {

	public byte[] name = new byte[6];
	public byte[] groupName = new byte[4];
	public byte[] ringName = new byte[4]; 
	public long id;
	
	public SeqSqfMonPortMaint() {
		super();
		msgType = MsgType.PORT_MAINT;
		this.clear();
	}
	
	@Override
	public void clear() {
		Arrays.fill(this.name, (byte) ' ');
		Arrays.fill(this.groupName, (byte) ' ');
		Arrays.fill(this.ringName, (byte) ' ');
		this.id = 0;
	}

}
