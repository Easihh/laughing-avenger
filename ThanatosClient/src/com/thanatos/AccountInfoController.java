package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class AccountInfoController implements Initializable{
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void refreshMonitor() {
		System.out.println("Refresh Account Info Table");
	}
}
