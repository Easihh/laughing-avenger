package com.thanatos;
	
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanatos.shared.RemoteOrder;
import com.thanatos.shared.RmiLoginIntf;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static ApplicationContext ctx;
	private Connection connection; 
	private RemoteOrderProducer orderProducer;
	private RefreshQueueConsumer refreshConsumer;
	
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
	}
}
