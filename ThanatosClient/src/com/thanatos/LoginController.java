package com.thanatos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML
	private AnchorPane rootPane;
	private Double screenWidth;
	private Double screenHeight;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void login(){	
		System.out.println("LOG ME");
		/*Alert myAlert=new Alert(AlertType.ERROR);
		myAlert.setHeaderText("Connection Error");
		myAlert.setContentText("Failed To Connect to Login Server");
		myAlert.show();*/
		try{
			FXMLLoader loader = new FXMLLoader(
			        getClass().getResource("/MainView.fxml")
			);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			screenWidth=primaryScreenBounds.getWidth();
			screenHeight=primaryScreenBounds.getHeight();
			Stage current=(Stage)rootPane.getScene().getWindow();
			current.setTitle("Thanatos");
			Scene scene = new Scene((Parent)loader.load(),600,800);
			current.setScene(scene);
			//Main.primaryStage.setTitle("Thanatos");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//current.setMaximized(true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
