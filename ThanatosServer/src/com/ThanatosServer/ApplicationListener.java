package com.ThanatosServer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ApplicationListener implements ServletContextListener {
	
	private RemoteLoginListener loginListener;
	private OrderListener orderListener;
	
	@Override
	public void contextDestroyed(ServletContextEvent context) {
	
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {

		System.out.println("I WAS INITIALIZED");
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("localhost");
		try{
			Connection connection=factory.newConnection();
			loginListener=new RemoteLoginListener(connection);
			orderListener=new OrderListener(connection);
			System.out.println("[*] Waiting for messages. To exit press CTRL+C");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
