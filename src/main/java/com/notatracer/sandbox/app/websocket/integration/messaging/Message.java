package com.notatracer.sandbox.app.websocket.integration.messaging;

public class Message {

	public enum MsgType {
		PORT_ACTIVITY((byte)'P'),
		PORT_CONNECTION_EVENT((byte)'C'),
		PORT_MAINT((byte)'M');
		
		private byte val;

		private MsgType(byte val) {
			this.val = val;
		}
		
		public byte getVal() {
			return this.val;
		}
		
	}
	
	public enum ConnectionEvent {
		DISCONNECT((byte)0),
	    CONNECT((byte)1);
		
	    private byte val;
		
		private ConnectionEvent(byte val) {
			this.val = val;
		}

		public static ConnectionEvent forValue(byte val) {
			switch (val) {
			case 0:
				return ConnectionEvent.DISCONNECT;
			case 1:
				return ConnectionEvent.CONNECT;
			default:
				throw new IllegalArgumentException();
			}
		}
		
		public byte getVal() {
			return val;
		}
		
	}
	
	public MsgType msgType;

}
