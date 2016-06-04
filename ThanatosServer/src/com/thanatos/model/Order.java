package com.thanatos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
	private int id;
	private StringProperty symbol;
	private IntegerProperty qty;
	
	public int getId() {
		return id;
	}
	public Order(String symbol,int quantity){
		this.symbol=new SimpleStringProperty(symbol);
		qty=new SimpleIntegerProperty(quantity);
	}
	public StringProperty symbolProperty(){
		return symbol;
	}
	public String getSymbol() {
		return symbolProperty().get();
	}
	public void setSymbol(String symbol) {
		this.symbol.set(symbol);
	}
	public IntegerProperty qtyProperty() {
		return qty;
	}
	public int getQty() {
		return qtyProperty().get();
	}
	public void setQty(int quantity) {
		qty.set(quantity);
	}
}
