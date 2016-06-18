package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.rabbitmq.client.Connection;
import com.thanatos.shared.RmiLoginIntf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML
	private AnchorPane rootPane;
	private Double screenWidth;
	private Double screenHeight;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	public void login(){	
		System.out.println("LOG ME");
		try{
			Registry myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			RmiLoginIntf login=(RmiLoginIntf)myReg.lookup("login");
			if(login.login(username.getText(), password.getText())){
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
				scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
				MainController controller=(MainController)loader.getController();
				controller.setupMq();
			}
			else showErrorMessage("Wrong Username/Password");
			//current.setMaximized(true);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("Unable to Connect to Login Server");
		}
	}

	private void showErrorMessage(String content) {
		Alert myAlert=new Alert(AlertType.ERROR);
		myAlert.setHeaderText("Connection Error");
		myAlert.setContentText(content);
		myAlert.show();
	}
}
