package com.notatracer.sandbox.app.websocket.domain;

import java.math.BigDecimal;

public class PortActivity extends SQFMonDomainObject {

	public String last;
	public String name;
	public String groupName;
	public String ringName;
	public long rateCurrent;
	public long rate1Min;
	public long rate5Min;
	
	public BigDecimal qlCurrent;
	public BigDecimal ql1Min;
	public BigDecimal limitCurrent;
	public BigDecimal limit1Min;
	public BigDecimal limit5Min;
	
	public long blocks;
	public long numQuotes;
	public long numBlocks;
	public long numPurges;
	public long numUndPurges;
	
	public PortActivity() {
		super();
	}
	
	public PortActivity(String name, String groupName, String ringName, long rateCurrent, long rate1Min, long rate5Min,
			BigDecimal qlCurrent, BigDecimal ql1Min, BigDecimal limitCurrent, BigDecimal limit1Min,
			BigDecimal limit5Min, long numQuotes, long numBlocks, long numPurges, long numUndPurges) {
		super();
		this.name = name;
		this.groupName = groupName;
		this.ringName = ringName;
		this.rateCurrent = rateCurrent;
		this.rate1Min = rate1Min;
		this.rate5Min = rate5Min;
		this.qlCurrent = qlCurrent;
		this.ql1Min = ql1Min;
		this.limitCurrent = limitCurrent;
		this.limit1Min = limit1Min;
		this.limit5Min = limit5Min;
		this.numQuotes = numQuotes;
		this.numBlocks = numBlocks;
		this.numPurges = numPurges;
		this.numUndPurges = numUndPurges;
	}
	public String getName() {
		return name;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getRingName() {
		return ringName;
	}
	public long getRateCurrent() {
		return rateCurrent;
	}
	public long getRate1Min() {
		return rate1Min;
	}
	public long getRate5Min() {
		return rate5Min;
	}
	public BigDecimal getQlCurrent() {
		return qlCurrent;
	}
	public BigDecimal getQl1Min() {
		return ql1Min;
	}
	public BigDecimal getLimitCurrent() {
		return limitCurrent;
	}
	public BigDecimal getLimit1Min() {
		return limit1Min;
	}
	public BigDecimal getLimit5Min() {
		return limit5Min;
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
	public long getBlocks() {
		return blocks;
	}
	public String getLast() {
		return last;
	}
	
}
