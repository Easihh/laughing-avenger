package com.thanatos.model;

import java.util.ArrayList;
import java.util.List;

import com.thanatos.shared.RmiOrder;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
	private int id;
	private StringProperty symbol;
	private IntegerProperty qty;
	private StringProperty orderStatus;
	private IntegerProperty ticketID;
	public int getId() {
		return id;
	}
		
	public Order(RmiOrder rmi) {
		symbol=new SimpleStringProperty(rmi.getSymbol());
		qty=new SimpleIntegerProperty(rmi.getQuantity());
		orderStatus=new SimpleStringProperty(rmi.getOrderStatus());
		ticketID=new SimpleIntegerProperty(rmi.getTicketID());
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
	
	public StringProperty orderStatusProperty(){
		return orderStatus;
	}
	
	public String getOrderStatus() {
		return orderStatusProperty().get();
	}
	
	public void setOrderStatus(String orderStatus) {
		this.orderStatus.set(orderStatus);
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
	
	public IntegerProperty ticketIDProperty() {
		return ticketID;
	}
	
	public int getTicketID() {
		return qtyProperty().get();
	}
	
	public void setTicketID(int ticketID) {
		this.ticketID.set(ticketID);
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
