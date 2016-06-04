package com.thanatos.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Users {

    private StringProperty firstName;

    private StringProperty lastName;

    private StringProperty email;
    
	private String username;

    public Users(){}
	public Users(String fName, String lName, String email) {
	
	    this.firstName = new SimpleStringProperty(fName);
	
	    this.lastName = new SimpleStringProperty(lName);
	
	    this.email = new SimpleStringProperty(email);
	
	}    

    public StringProperty firstNameProperty() { return firstName; }

    public StringProperty lastNameProperty() { return lastName; }

    public String getUsername() { return username; }
    
    public StringProperty emailProperty() { return email; }
    
	public void setUsername(String username) {
		this.username = username;
	}

}