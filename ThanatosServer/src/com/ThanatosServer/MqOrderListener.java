package com.ThanatosServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class MqOrderListener{
	
	private Connection myConnection;
	private Channel channel;
	private Consumer consumer;
	private final static String ORDER_QUEUE_NAME="SINGLE_ORDER";
	private final static String EXCHANGE="ORDER";
	private String channelTag;
	public MqOrderListener(Connection connection) {
		try {
				myConnection=connection;
				channel=myConnection.createChannel();
				channel.basicQos(1);
				channel.queueBind(ORDER_QUEUE_NAME, EXCHANGE, ORDER_QUEUE_NAME);
				setupConsumer();
			} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}


	private void setupConsumer() throws IOException {
	    consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		          throws IOException {
		        String message = new String(body, "UTF-8");
		        String response="ORDER RESPONSE";
		        System.out.println(" [x] Order Info Received From Client '" + message + "'");
		         BasicProperties props=new BasicProperties.Builder()
					.contentType("ORDER")
	 				.replyTo(properties.getReplyTo())
	 				.build();
				channel.basicPublish("",properties.getReplyTo(),props, response.getBytes());				
				//channel.basicAck(envelope.getDeliveryTag(), false);
				System.out.println("[x] Order Info Sent To Client");
		      }
		    };
		channelTag=channel.basicConsume(ORDER_QUEUE_NAME, true, consumer);
		channel.addShutdownListener(new ShutdownListener() {		
			@Override
			public void shutdownCompleted(ShutdownSignalException e) {
				System.out.println("Order Listener Error:"+e.getMessage());
			}
		});
	}
	public void close() throws IOException, TimeoutException{
		channel.basicCancel(channelTag);
		channel.close();
	}
}
