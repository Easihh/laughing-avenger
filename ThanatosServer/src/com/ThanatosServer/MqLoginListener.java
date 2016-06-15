package com.ThanatosServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
public class MqLoginListener{
	
	private Connection myConnection;
	private Channel channel;
	private final static String RPC_QUEUE_NAME="TEST";
	private Consumer consumer;
	private final static String EXCHANGE="LOGIN";
	private String channelTag;
	
	public MqLoginListener(Connection connection) {
		myConnection=connection;
		try {
				channel=myConnection.createChannel();
				channel.basicQos(1);
				channel.queueBind(RPC_QUEUE_NAME, EXCHANGE, RPC_QUEUE_NAME);
				setupConsumer();
			} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void setupConsumer() throws IOException {
	    consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	            throws IOException {
	         String message = new String(body, "UTF-8");
	         System.out.println("Received Login Connection Request" + message + "'");
	         String response="OK";
	         BasicProperties props=new BasicProperties.Builder()
	         						.contentType("LOGIN")
	        		 				.replyTo(properties.getReplyTo())
	        		 				.build();
			 channel.basicPublish("",properties.getReplyTo(),props, response.getBytes());				
			 //channel.basicAck(envelope.getDeliveryTag(), false);
	         System.out.println(response);
	        }
	      };
	    channelTag=channel.basicConsume(RPC_QUEUE_NAME, true, consumer);
	}
	public void close() throws IOException, TimeoutException{
		channel.basicCancel(channelTag);
		channel.close();
	}
}
