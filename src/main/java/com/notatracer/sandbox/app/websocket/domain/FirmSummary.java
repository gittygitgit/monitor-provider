package com.notatracer.sandbox.app.websocket.domain;

import java.math.BigDecimal;

public class FirmSummary {

	public String portName;
	public String name;
	public BigDecimal latency;
	public long numQuotes;
	public long numBlocks;
	public long numPurges;
	public long numUndPurges;
	
	public FirmSummary() {
	}
	
	public FirmSummary(String portName, String name, BigDecimal latency, int numQuotes, int numBlocks, int numPurges,
			int numUndPurges) {
		super();
		this.portName = portName;
		this.name = name;
		this.latency = latency;
		this.numQuotes = numQuotes;
		this.numBlocks = numBlocks;
		this.numPurges = numPurges;
		this.numUndPurges = numUndPurges;
	}
	
	public String getPortName() {
		return portName;
	}
	public String getName() {
		return name;
	}
	public BigDecimal getLatency() {
		return latency;
	}
	public long getNumQuotes() {
		return numQuotes;
	}
	public long getNumBlocks() {
		return numBlocks;
	}
	public long getNumPurges() {
		return numPurges;
	}
	public long getNumUndPurges() {
		return numUndPurges;
	}
	
}
