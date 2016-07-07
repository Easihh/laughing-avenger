package com.thanatos.model;

import java.util.ArrayList;
import java.util.List;

import com.thanatos.shared.RmiOrder;
import com.thanatos.shared.RmiQuote;

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
	
	public Order(RmiOrder rmi) {
		symbol=new SimpleStringProperty(rmi.getSymbol());
		qty=new SimpleIntegerProperty(rmi.getQuantity());
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
	
	
	
	public static List<Order> rmiOrdertoOrder(List<RmiOrder> rmiOrders){
		List<Order> retVal=new ArrayList<Order>();
		for(RmiOrder rmi:rmiOrders){
			Order myOrder=new Order(rmi);
			retVal.add(myOrder);
		}
		return retVal;
	}
}
