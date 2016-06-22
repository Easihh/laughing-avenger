package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.model.Order;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class OpenOrderController implements Initializable{
	
	@FXML
	public TableView<Order> openTradeTableView;

    private ObservableList<Order> openOrders;
    @FXML
    private AnchorPane	accountInfo;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void refreshMonitor() {
		System.out.println("Refresh Open Order Table");
	}
}
