package com.thanatos;
	
import java.io.IOException;
import java.util.Date;
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
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;
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
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			ConnectionFactory factory=new ConnectionFactory();
			factory.setRequestedHeartbeat(30);
			factory.setHost("localhost");
			connection =factory.newConnection();		
			userLogin=new RemoteLoginProducer(connection);
			clientConsumer=new ClientQueueConsumer(connection,userLogin.getReplyQueueName());
			//userLogin.call("TEST");
			//userLogin.call("TEST2");
			order=new RemoteOrderProducer(connection,userLogin.getReplyQueueName());
			order.sendOrder(new RemoteOrder());
			//order.call("Some Order Info2");
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
            FXMLLoader loader = new FXMLLoader();
            Main.primaryStage=primaryStage;
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			screenWidth=primaryScreenBounds.getWidth();
			screenHeight=primaryScreenBounds.getHeight();
            loader.setLocation(getClass().getResource("TestOverview.fxml"));
            root = (AnchorPane) loader.load();
			Scene scene = new Scene(root,600,800);
			primaryStage.setTitle("Thanatos:Client");
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
