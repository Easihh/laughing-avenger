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

public class ClientQueueConsumer {
	
	private Consumer consumer;
	private String replyQueueName;
	private Channel channel;
	private Connection myConnection;
	private final static String ORDER="ORDER";
	
	public ClientQueueConsumer(Connection connection, String clientQueue){
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
				replyQueueName=clientQueue;
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
		        	if(properties.getContentType().equals(ORDER))
		        		System.out.println("This is an Order");
		        	else System.out.println("Login callback");
		          //String message = new String(body, "UTF-8");
		        }
		      };
			channel.basicConsume(replyQueueName, true,consumer);
			channel.addShutdownListener(new ShutdownListener() {			
				@Override
				public void shutdownCompleted(ShutdownSignalException e) {	
					System.out.println("Error:"+e.getMessage());
				}
			});
	}
}
