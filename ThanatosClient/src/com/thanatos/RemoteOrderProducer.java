package com.thanatos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.thanatos.shared.RemoteOrder;
import com.thanatos.utility.FileUtil;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;

public class RemoteOrderProducer{
	
	private String queueName = "SINGLE_ORDER";
	private String corrId;
	private Channel channel;
	private Connection myConnection;
	private final static String EXCHANGE="ORDER";
	
	public RemoteOrderProducer(Connection connection) {
		try {
			myConnection=connection;
			channel=myConnection.createChannel();
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
	                                .build();

	    channel.basicPublish(EXCHANGE, queueName, props, FileUtil.toByte(remoteOrder));
	    
	    System.out.println("Order Sent");
	}
}
