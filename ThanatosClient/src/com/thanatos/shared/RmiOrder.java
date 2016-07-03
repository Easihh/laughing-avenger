package com.thanatos.shared;

import java.io.Serializable;

public class RmiOrder implements Serializable{

	private static final long serialVersionUID = 6270348239766271051L;
	
	private int id;
	private String symbol;
	private int quantity;

	private int orderStatus;
	
	public RmiOrder(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
			
}
