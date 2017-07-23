package com.notatracer.sandbox.app.websocket.domain;

public class PortConnectionEvent extends SQFMonDomainObject {
	public String portName;
	public String groupName;
	public String ringName;
	public String event;
	public String time;
	
	public PortConnectionEvent() {
		super();
	}
	public String getPortName() {
		return portName;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getRingName() {
		return ringName;
	}
	public String getEvent() {
		return event;
	}
	public String getTime() {
		return time;
	}
}
