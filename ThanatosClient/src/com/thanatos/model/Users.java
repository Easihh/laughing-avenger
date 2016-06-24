package com.thanatos.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Users {
    
	private StringProperty username;
	private DoubleProperty balance;
	private DoubleProperty profitLoss;
	
    public Users(){}
    
	public Users(String uname, Double bal,Double pLoss) {
	
	    this.username = new SimpleStringProperty(uname);
	    balance=new SimpleDoubleProperty(bal);
	    profitLoss=new SimpleDoubleProperty(pLoss);
	
	}    

    public StringProperty usernameProperty() { return username; }

    public void setUsername(String username) {this.username.set(username);}
    
	public String getUsername() {return usernameProperty().get();}
	
    public DoubleProperty balanceProperty() { return balance; }

    public void setUsername(Double bal) {this.balance.set(bal);}
    
	public Double getBalance() {return balanceProperty().get();}
	
    public DoubleProperty profitLossProperty() { return profitLoss; }

    public void setProfitLoss(Double pLoss) {this.profitLoss.set(pLoss);}
    
	public Double getProfitLoss() {return profitLossProperty().get();}
}