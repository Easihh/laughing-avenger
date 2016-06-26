package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.shared.RemoteOrder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewOrderController implements Initializable{
	
	@FXML
	private AnchorPane createOrderPane;
	@FXML
	private ComboBox<String> orderSide;
	@FXML
	private ComboBox<String> orderType;
	private RemoteOrderProducer producer;
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
			producer.sendOrder(new RemoteOrder());
			current.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setOrderProducer(RemoteOrderProducer myProducer){
		producer=myProducer;
	}
}
