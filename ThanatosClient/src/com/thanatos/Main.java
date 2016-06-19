package com.thanatos;
	
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static ApplicationContext ctx;
	
	@Override
	public void start(Stage primaryStage) {
		try {				
				ctx=new ClassPathXmlApplicationContext("Spring.xml");
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
