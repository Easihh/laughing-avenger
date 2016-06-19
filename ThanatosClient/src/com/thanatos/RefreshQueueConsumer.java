package com.thanatos;

import java.io.IOException;
import java.util.logging.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;

public class RefreshQueueConsumer {
	
	private Consumer consumer;
	private static String QUEUE;
	private Channel channel;
	private Connection myConnection;
	
	public RefreshQueueConsumer(Connection connection){
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
				QUEUE=channel.queueDeclare().getQueue();
				channel.queueBind(QUEUE, "REFRESH", "#");
				setupConsumer();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupConsumer() throws IOException {
	     consumer = new DefaultConsumer(channel) {
		        @Override
		        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		            throws IOException {
		          String message = new String(body, "UTF-8");
		          refreshMonitor();
		          System.out.println(message);
		        }
		      };
			channel.basicConsume(QUEUE, true,consumer);
			channel.addShutdownListener(new ShutdownListener() {			
				@Override
				public void shutdownCompleted(ShutdownSignalException e) {	
					System.out.println("Shutdown:"+e.getMessage());
				}
			});
	}

	private void refreshMonitor() {
		try {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("/quotePanel.fxml"));
			TitledPane pane=loader.load();
	        QuoteController controller=(QuoteController)loader.getController();
	        controller.refreshMonitor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
