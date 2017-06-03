package com.notatracer.sandbox.app.websocket.integration.messaging;

public class Message {

	public enum MsgType {
		FIRM_UPDATE((byte)'A');
		
		private byte val;

		private MsgType(byte val) {
			this.val = val;
		}
		
	}
	public MsgType msgType;

}
