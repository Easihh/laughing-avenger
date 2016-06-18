package com.thanatos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;

public class RefreshProducer{
	
	private String queueName = "REFRESHQ";
	private Channel channel;
	private Connection myConnection;
	private final static String EXCHANGE="REFRESH";
	
	public RefreshProducer(Connection connection) {
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendRefresh() throws IOException {
		String message="REFRESH";
	    channel.basicPublish(EXCHANGE, queueName, null, message.getBytes());
	    
	    System.out.println("Refresh Sent to All Clients");
	}
}
