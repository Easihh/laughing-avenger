package com.thanatos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RemoteLogin{
	
	private String requestQueueName = "TEST";
	private Consumer consumer;
	private String replyQueueName;
	private String corrId;
	private Channel channel;
	private boolean connectionResponse;
	private Connection myConnection;
	private final static String EXCHANGE="LOGIN";
	
	public RemoteLogin(Connection connection) {
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
				replyQueueName=channel.queueDeclare().getQueue();
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
	          System.out.println("Login Connection Request Answer:" + message);
	        }
	      };
		channel.basicConsume(replyQueueName, true,consumer);	
	}	
	public boolean call(String message) throws Exception {     
		connectionResponse = false;
	    corrId = java.util.UUID.randomUUID().toString();

	    BasicProperties props = new BasicProperties
	                                .Builder()
	                                .correlationId(corrId)
	                                .replyTo(replyQueueName)
	                                .build();

	    channel.basicPublish(EXCHANGE, requestQueueName, props, message.getBytes());
	    
	    System.out.println("Login Connection Request Sent");

	    return connectionResponse; 
	}
	
	public void close(){	
		try {
				channel.close();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getReplyQueueName() {
		return replyQueueName;
	}
}
