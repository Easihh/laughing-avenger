package com.thanatos.common.model;

import java.io.Serializable;
import java.util.Date;

public class RmiQuote implements Serializable,Comparable<RmiQuote>{

	private static final long serialVersionUID = -4922285857615635483L;
	private int id;
	private int volume;
	private Double lastPx;
	private String symbol;
	private Double dayHigh;
	private Double dayLow;
	private Double bid;
	private Double ask;
	private Double change;
	private Double prevClose;
	private Double open;
	private Date timeDate;
	
	public RmiQuote(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public Double getLastPx() {
		return lastPx;
	}

	public void setLastPx(Double lastPx) {
		this.lastPx = lastPx;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getDayHigh() {
		return dayHigh;
	}

	public void setDayHigh(Double dayHigh) {
		this.dayHigh = dayHigh;
	}

	public Double getDayLow() {
		return dayLow;
	}

	public void setDayLow(Double dayLow) {
		this.dayLow = dayLow;
	}

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

	public Double getAsk() {
		return ask;
	}

	public void setAsk(Double ask) {
		this.ask = ask;
	}

	public Double getChange() {
		return change;
	}

	public void setChange(Double change) {
		this.change = change;
	}

	public Double getPrevClose() {
		return prevClose;
	}

	public void setPrevClose(Double prevClose) {
		this.prevClose = prevClose;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}
	
	public Date getTimeDate() {
		return timeDate;
	}

	public void setTimeDate(Date timeDate) {
		this.timeDate = timeDate;
	}

	@Override
	public int compareTo(RmiQuote o) {
		return this.getId()>id ? 1:-1;
	}
		
}
