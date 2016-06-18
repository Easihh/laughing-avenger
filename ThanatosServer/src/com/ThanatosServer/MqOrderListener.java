package com.ThanatosServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import quickfix.SessionID;

import com.ThanatosServer.Utility.Util;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.thanatos.shared.RemoteOrder;
import com.thanatos.shared.TranslatorToFix;

public class MqOrderListener{
	
	private Connection myConnection;
	private Channel channel;
	private Consumer consumer;
	private final static String ORDER_QUEUE_NAME="SINGLE_ORDER";
	private final static String EXCHANGE="ORDER";
	private String channelTag;
	private SessionID sessionId;
	public MqOrderListener(Connection connection, SessionID sessionID) {
		try {
				sessionId=sessionID;
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
		        RemoteOrder myOrder=(RemoteOrder)Util.convertFromBytes(body);
		    	String message = new String(body, "UTF-8");
		        System.out.println(" [x] Order Info Received From Client '" + myOrder.toString() + "'");
		        TranslatorToFix fix=new TranslatorToFix(myOrder,sessionId);
		      }
		    };
		channelTag=channel.basicConsume(ORDER_QUEUE_NAME, true, consumer);
		channel.addShutdownListener(new ShutdownListener() {		
			@Override
			public void shutdownCompleted(ShutdownSignalException e) {
				System.out.println("Order Listener Shutdown:"+e.getMessage());
			}
		});
	}
	public void close() throws IOException, TimeoutException{
		channel.basicCancel(channelTag);
		channel.close();
	}
}
