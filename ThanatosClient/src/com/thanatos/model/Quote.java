package com.thanatos.model;

import java.util.Date;

public class Quote {
	private int id;
	private Date date;
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
	public Quote(String[] dataArr) {
		symbol=dataArr[0].replaceAll("^\"|\"$","");
		lastPx=Double.valueOf(dataArr[1]);
		change=Double.valueOf(dataArr[4]);
		open=Double.valueOf(dataArr[5]);
		dayHigh=Double.valueOf(dataArr[6]);
		dayLow=Double.valueOf(dataArr[7]);
		volume=Integer.valueOf(dataArr[8]);
		prevClose=Double.valueOf(dataArr[9]);
		bid=Double.valueOf(dataArr[10]);
		ask=Double.valueOf(dataArr[11]);
	}
	public int getId() {
		return id;
	}
}
