package com.thanatos;
	
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanatos.shared.RemoteOrder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	
	private AnchorPane root;
	public static Stage primaryStage;
	public static ApplicationContext ctx;
	public static Double screenWidth;
	public static Double screenHeight;
	private Connection connection; 
	private RemoteLoginProducer userLogin;
	private RemoteOrderProducer order;
	private ClientQueueConsumer clientConsumer;
	public static FXMLLoader loader;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			ConnectionFactory factory=new ConnectionFactory();
			factory.setRequestedHeartbeat(30);
			factory.setHost("localhost");
			connection =factory.newConnection();		
			userLogin=new RemoteLoginProducer(connection);
			Registry myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			//RmiLoginIntf a=(RmiLoginIntf)myReg.lookup("login");
			//System.out.println("Login:"+a.login("",""));
			clientConsumer=new ClientQueueConsumer(connection,userLogin.getReplyQueueName());
			//userLogin.call("TEST");
			//userLogin.call("TEST2");
			order=new RemoteOrderProducer(connection,userLogin.getReplyQueueName());
			order.sendOrder(new RemoteOrder());
			//order.call("Some Order Info2");
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
            Main.loader = new FXMLLoader();
            Main.primaryStage=primaryStage;
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			screenWidth=primaryScreenBounds.getWidth();
			screenHeight=primaryScreenBounds.getHeight();
            loader.setLocation(getClass().getResource("LoginView.fxml"));
            root = (AnchorPane) loader.load();
			Scene scene = new Scene(root,500,400);
			primaryStage.setTitle("Thanatos");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			//primaryStage.setMaximized(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
		userLogin.close();
		try {
				connection.close();
			} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
