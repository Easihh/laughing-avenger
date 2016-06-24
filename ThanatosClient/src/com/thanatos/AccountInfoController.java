package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.model.Order;
import com.thanatos.model.Users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class AccountInfoController implements Initializable{
	    
	public TableView<Users> accountTableView;
	private ObservableList<Users> user;
    @FXML
    public TableColumn<?, ?> accountCol;
    @FXML
    public TableColumn<?, ?> balanceCol;
    @FXML
    public TableColumn<?, ?> profitLoss;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user=FXCollections.observableArrayList();
		user.add(new Users("D112312",9999.0,9999.0));
		accountCol.setCellValueFactory(new PropertyValueFactory("username"));
		balanceCol.setCellValueFactory(new PropertyValueFactory("balance"));
		profitLoss.setCellValueFactory(new PropertyValueFactory("profitLoss"));
		accountTableView.setItems(user);
	}

	public void refreshMonitor() {
		System.out.println("Refresh Account Info Table");
	}
}
