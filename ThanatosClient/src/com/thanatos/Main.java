package com.thanatos;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {				
			LoginManager manager=new LoginManager(primaryStage);
			manager.showLoginScreen();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
		Platform.exit();
		System.exit(0);
	}
}
