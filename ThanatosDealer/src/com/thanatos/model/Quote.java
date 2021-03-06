package com.thanatos.model;

import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Quote {
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
	private final static int YAHOO_DELAY=16;
	public Quote(){}//MyBatis Constructor
	
	public Quote(String[] dataArr) {
		for(int i=0;i<dataArr.length;i++)
			dataArr[i]=dataArr[i].replaceAll("^\"|\"$","");
		symbol=dataArr[0];
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MINUTE, -YAHOO_DELAY);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		timeDate = new Date(cal.getTimeInMillis());
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
	
	public IntegerProperty volumeProperty(){
		return new SimpleIntegerProperty(volume);
	}
	
	public StringProperty symbolProperty(){
		return new SimpleStringProperty(symbol);
	}
	
	public DoubleProperty openProperty(){
		return new SimpleDoubleProperty(open);
	}
	
	public DoubleProperty lastPxProperty(){
		return new SimpleDoubleProperty(lastPx);
	}
	
	public DoubleProperty dayHighProperty(){
		return new SimpleDoubleProperty(dayHigh);
	}
	
	public DoubleProperty dayLowProperty(){
		return new SimpleDoubleProperty(dayLow);
	}
	
	public DoubleProperty bidProperty(){
		return new SimpleDoubleProperty(bid);
	}
	
	public DoubleProperty askProperty(){
		return new SimpleDoubleProperty(ask);
	}
	
	public DoubleProperty changeProperty(){
		return new SimpleDoubleProperty(change);
	}
	
	public DoubleProperty prevCloseProperty(){
		return new SimpleDoubleProperty(prevClose);
	}
}
