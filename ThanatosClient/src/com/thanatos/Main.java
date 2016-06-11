package com.thanatos;
	
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

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
	private SocketInitiator initiator;
	private Channel channel;
	private String requestQueueName = "TEST";
	private QueueingConsumer consumer;
	private String replyQueueName;
	private Connection connection; 
	@Override
	public void start(Stage primaryStage) {
		try {
			
			ConnectionFactory factory=new ConnectionFactory();
			factory.setRequestedHeartbeat(30);
			factory.setHost("localhost");
			connection =factory.newConnection();
			channel=connection.createChannel();
			replyQueueName=channel.queueDeclare().getQueue();
			consumer=new QueueingConsumer(channel);
			channel.basicConsume(replyQueueName, true,consumer);
			String result=call("TEST");
			//channel.queueDeclare("TEST",true,false,false,null);
			//String message="Hello World!";
			//channel.basicPublish("", "TEST", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			//System.out.println("[x]Sent '"+message+"'");
			FixClient client=new FixClient();
			SessionSettings settings = new SessionSettings("Client.cfg"); 
			FileStoreFactory storeFactory = new FileStoreFactory(settings); 
			ScreenLogFactory logFactory = new ScreenLogFactory(settings); 
			initiator = new SocketInitiator(client, storeFactory, settings, 
				    logFactory, new DefaultMessageFactory()); 
			initiator.start(); 
			Thread.sleep(3000); 
			SessionID sessionID = new SessionID("FIX.4.4","CLIENT1","FixServer");
			Session.lookupSession(sessionID).logon();
			NewOrderSingle order = new NewOrderSingle();
			order.set(new HandlInst(HandlInst.MANUAL_ORDER));
			order.set(new ClOrdID("DLF")); 
			order.set(new Symbol("DLF")); 
		    order.set(new Side(Side.BUY)); 
		    order.set(new TransactTime(new Date()));
		    order.set(new OrdType(OrdType.LIMIT)); 
			order.set(new OrderQty(45)); 
			order.set(new Price(25.4d)); 
			Session.sendToTarget(order, sessionID);
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
			primaryStage.setMaximized(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String call(String message) throws Exception {     
	    String response = null;
	    String corrId = java.util.UUID.randomUUID().toString();

	    BasicProperties props = new BasicProperties
	                                .Builder()
	                                .correlationId(corrId)
	                                .replyTo(replyQueueName)
	                                .build();

	    channel.basicPublish("", requestQueueName, props, message.getBytes());

	    while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        if (delivery.getProperties().getCorrelationId().equals(corrId)) {
	            response = new String(delivery.getBody());
	            System.out.println("Server Response:"+response);
	            break;
	        }
	    }

	    return response; 
	}
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
		initiator.stop();
		try {
			channel.close();
			connection.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
