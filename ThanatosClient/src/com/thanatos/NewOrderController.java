package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.Dao.OrderDao;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewOrderController implements Initializable{
	
	private OrderDao orderDao;
	@FXML
	private AnchorPane createOrderPane;
	@FXML
	private ComboBox<String> orderSide;
	@FXML
	private ComboBox<String> orderType;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderSide.setOnAction((event) ->{
			String selected = orderSide.getSelectionModel().getSelectedItem();
			    System.out.println("CheckBox Action (selected: " + selected + ")");
		});
		orderType.setOnAction((event) ->{
			String selected = orderType.getSelectionModel().getSelectedItem();
			    System.out.println("CheckBox Action (selected: " + selected + ")");
		});
	}
	
	public void createOrder(){
		try{
			Stage current=(Stage)createOrderPane.getScene().getWindow();
			current.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
