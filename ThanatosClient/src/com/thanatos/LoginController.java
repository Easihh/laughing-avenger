package com.thanatos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.Dao.OrderDao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML
	private AnchorPane root;
	private Double screenWidth;
	private Double screenHeight;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void login(){	
		System.out.println("LOG ME");
		try{
	        FXMLLoader loader = new FXMLLoader();
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			screenWidth=primaryScreenBounds.getWidth();
			screenHeight=primaryScreenBounds.getHeight();
	        loader.setLocation(getClass().getResource("TestOverview.fxml"));
	        root = (AnchorPane) loader.load();
			Scene scene = new Scene(root,600,800);
			Main.primaryStage.setTitle("Thanatos");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
			//primaryStage.setMaximized(true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
