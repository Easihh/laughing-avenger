package com.thanatos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
	private StringProperty symbol;
	private IntegerProperty qty;
	
	public Order(String symbol,int quantity){
		this.symbol=new SimpleStringProperty(symbol);
		qty=new SimpleIntegerProperty(quantity);
	}
	public StringProperty symbolProperty(){
		return symbol;
	}
	public IntegerProperty qtyProperty() {
		return qty;
	}
}
