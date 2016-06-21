package com.thanatos;

import java.io.IOException;

import com.rabbitmq.client.Connection;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginManager {
	
	private Stage stage;
	
	public LoginManager(Stage primaryStage){
		stage=primaryStage;
	}

	public void showLoginScreen() {
		Scene scene=new Scene(new AnchorPane());
		FXMLLoader loader = new FXMLLoader(
		        getClass().getResource("/LoginView.fxml")
		);
		try {
			scene.setRoot((Parent)loader.load());
			stage.setScene(scene);
			stage.setTitle("Thanatos:Login");
			stage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
