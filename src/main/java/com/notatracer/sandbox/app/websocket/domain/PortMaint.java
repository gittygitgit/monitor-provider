package com.notatracer.sandbox.app.websocket.domain;

public class PortMaint extends SQFMonDomainObject {

	private long id;
	private String groupName;
	private String name;
	private String ringName;
	
	public PortMaint() {
		super();
	}
	
	public PortMaint(long id, String groupName, String name, String ringName) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.name = name;
		this.ringName = ringName;
	}
	
	public long getId() {
		return id;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getName() {
		return name;
	}
	public String getRingName() {
		return ringName;
	}
	
}
