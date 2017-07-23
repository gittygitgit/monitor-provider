package com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor;

import java.util.Arrays;

public class SeqSqfMonPortActivity extends SeqSqfMonMsg {

	public byte[] last = new byte[12];
	public byte[] name = new byte[6];
	public byte[] groupName = new byte[4];
	public byte[] ringName = new byte[4]; 
	public long rateCurrent;
	public long rate1Min;
	public long rate5Min;

	public long qlCurrent;
	public long ql1Min;
	public long limitCurrent;
	public long limit1Min;
	public long limit5Min;
	public long blocks;
	public long numQuotes;
	public long numBlocks;
	public long numPurges;
	public long numUndPurges;

	public SeqSqfMonPortActivity() {
		super();
		msgType = MsgType.PORT_ACTIVITY;
		this.clear();
	}

	public void clear() {

		Arrays.fill(this.name, (byte) ' ');
		Arrays.fill(this.groupName, (byte) ' ');
		Arrays.fill(this.ringName, (byte) ' ');
		Arrays.fill(this.last, (byte) ' ');

		this.rateCurrent = 0;
		this.rate1Min = 0;
		this.rate5Min = 0;

		this.qlCurrent = 0;
		this.ql1Min = 0;
		this.limitCurrent = 0;
		this.limit1Min = 0;
		this.limit5Min = 0;
		this.blocks = 0l;
		this.numQuotes = 0l;
		this.numBlocks = 0l;
		this.numPurges = 0;
		this.numUndPurges = 0;
	}
}
