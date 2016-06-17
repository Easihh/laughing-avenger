package com.thanatos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.thanatos.shared.RemoteOrder;
import com.thanatos.utility.Util;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;

public class RemoteOrderProducer{
	
	private String requestQueueName = "SINGLE_ORDER";
	private String replyQueueName;
	private String corrId;
	private Channel channel;
	private Connection myConnection;
	private final static String EXCHANGE="ORDER";
	
	public RemoteOrderProducer(Connection connection,String clientQueue) {
		try {
				replyQueueName=clientQueue;
				myConnection=connection;
				channel=myConnection.createChannel();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void call(String message) throws Exception {     
	    corrId = java.util.UUID.randomUUID().toString();

	    BasicProperties props = new BasicProperties
	                                .Builder()
	                                .correlationId(corrId)
	                                .replyTo(replyQueueName)
	                                .build();

	    channel.basicPublish(EXCHANGE, requestQueueName, props, message.getBytes());
	    
	    System.out.println("Order Sent");

	}
	
	public void close(){	
		try {
				channel.close();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendOrder(RemoteOrder remoteOrder) throws IOException {
	    corrId = java.util.UUID.randomUUID().toString();

	    BasicProperties props = new BasicProperties
	                                .Builder()
	                                .correlationId(corrId)
	                                .replyTo(replyQueueName)
	                                .build();

	    channel.basicPublish(EXCHANGE, requestQueueName, props, Util.toByte(remoteOrder));
	    
	    System.out.println("Order Sent");
	}
}
