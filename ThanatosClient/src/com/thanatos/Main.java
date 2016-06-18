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
	private RemoteOrderProducer order;
	private RefreshQueueConsumer refreshConsumer;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			ConnectionFactory factory=new ConnectionFactory();
			factory.setRequestedHeartbeat(30);
			factory.setHost("localhost");
			connection =factory.newConnection();		
			//Registry myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			//RmiLoginIntf a=(RmiLoginIntf)myReg.lookup("login");
			//System.out.println("Login:"+a.login("",""));
			refreshConsumer=new RefreshQueueConsumer(connection);
			order=new RemoteOrderProducer(connection);
			order.sendOrder(new RemoteOrder());
			order.sendOrder(new RemoteOrder());
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
			LoginManager manager=new LoginManager(primaryStage);
			manager.showLoginScreen();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
		try {
				connection.close();
			} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
