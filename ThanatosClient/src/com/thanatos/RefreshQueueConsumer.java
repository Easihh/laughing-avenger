package com.thanatos;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RefreshQueueConsumer {
	
	private Consumer consumer;
	private final static String QUEUE="REFRESHQ";
	private Channel channel;
	private Connection myConnection;
	private final static String REFRESH="REFRESH";
	
	public RefreshQueueConsumer(Connection connection){
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
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
		          System.out.println(message);
		        }
		      };
			channel.basicConsume(QUEUE, true,consumer);
			channel.addShutdownListener(new ShutdownListener() {			
				@Override
				public void shutdownCompleted(ShutdownSignalException e) {	
					System.out.println("Error:"+e.getMessage());
				}
			});
	}
}
