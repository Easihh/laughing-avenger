package com.thanatos.shared;

import java.io.Serializable;

public class RmiOrder implements Serializable{

	private static final long serialVersionUID = 6270348239766271051L;
	
	private int id;
	private String symbol;
	private int quantity;
	private int ticketID;
	private String  orderStatus;
	
	public RmiOrder(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTicketID() {
		return ticketID;
	}

	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
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
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
			
}
