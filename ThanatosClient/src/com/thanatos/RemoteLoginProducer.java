package com.thanatos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RemoteLoginProducer{
	
	private String requestQueueName = "TEST";
	private String replyQueueName;
	private String corrId;
	private Channel channel;
	private boolean connectionResponse;
	private Connection myConnection;
	private final static String EXCHANGE="LOGIN";
	
	public RemoteLoginProducer(Connection connection) {
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
				replyQueueName=channel.queueDeclare().getQueue();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
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
