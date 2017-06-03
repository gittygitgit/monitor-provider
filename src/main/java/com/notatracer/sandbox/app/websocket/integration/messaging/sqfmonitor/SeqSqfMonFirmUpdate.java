package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.util.Arrays;

import com.notatracer.sandbox.app.websocket.integration.messaging.Message.MsgType;

public class SeqSqfMonFirmUpdate extends SeqSqfMonMsg {

	public byte[] portName = new byte[6];
	public byte[] firmName = new byte[6];
	public long latency;
	public  long numQuotes;
	public long numBlocks;
	public int numPurges;
	public int numUndPurges;

	
	public SeqSqfMonFirmUpdate() {
		super();
		msgType = MsgType.FIRM_UPDATE;
		this.clear();
	}

	public void clear() {
		this.latency = 0l;
		this.numQuotes = 0l;
		this.numBlocks = 0l;
		this.numPurges = 0;
		this.numUndPurges = 0;
		Arrays.fill(portName, (byte)' ');
		Arrays.fill(firmName, (byte)' ');
	}
}
