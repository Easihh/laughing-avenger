package com.thanatos.model;

import java.util.ArrayList;
import java.util.List;

import com.thanatos.shared.RmiQuote;

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
	
	public Quote(){}//MyBatis Constructor
	
	public Quote(RmiQuote rmi){
		symbol=rmi.getSymbol();
		lastPx=rmi.getLastPx();
		change=rmi.getChange();
		open=rmi.getOpen();
		dayHigh=rmi.getDayHigh();
		dayLow=rmi.getDayLow();
		volume=rmi.getVolume();
		prevClose=rmi.getPrevClose();
		bid=rmi.getBid();
		ask=rmi.getAsk();	
	}
	
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

	public static List<Quote> rmiOrderToOrder(List<RmiQuote> rmiQuotes) {
		List<Quote> retVal=new ArrayList<Quote>();
		for(RmiQuote rmi:rmiQuotes){
			Quote myQuote=new Quote(rmi);
			retVal.add(myQuote);
		}
		return retVal;
	}
}
