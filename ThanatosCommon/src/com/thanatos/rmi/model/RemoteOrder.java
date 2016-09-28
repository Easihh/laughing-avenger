package com.thanatos.rmi.model;

import java.io.Serializable;

public class RemoteOrder implements Serializable{
	
	private static final long serialVersionUID = 1678548238557708105L;	
	private int quantity;
	private String symbol;
	private OrderSide orderSide;
	private OrderType orderType;
	
	public RemoteOrder(){
		quantity=100;
		symbol="GOOG";
		orderSide=OrderSide.SELL;
		orderType=OrderType.MARKET;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	@Override
	public String toString(){
		String retVal="quantity:"+quantity+" symbol:"+symbol+
				" orderSide:"+orderSide.toString()+" orderType:"+
				orderType.toString();
		return retVal;	
	}
}
