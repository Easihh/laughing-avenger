package com.thanatos.shared;

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
	
	@Override
	public String toString(){
		String retVal="quantity:"+quantity+" symbol:"+symbol+
				" orderSide:"+orderSide.toString()+" orderType:"+
				orderType.toString();
		return retVal;	
	}
}
