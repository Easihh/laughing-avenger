package com.thanatos.main;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanatos.daoImpl.RmiLoginImpl;
import com.thanatos.daoImpl.RmiOrderImpl;
import com.thanatos.daoImpl.RmiQuoteImpl;

@SuppressWarnings("unused")
@WebListener
public class ApplicationListener implements ServletContextListener {
	
	private MqOrderListener orderListener;
	private ConnectionFactory factory;
	private Connection connection;
	private SocketInitiator initiator;
	private static final String SENDER="CLIENT1";
	private static final String TARGET="FixDealer";
	private static final String FIX_VERSION="FIX.4.4";
	private ApplicationContext myContext;
	private int targetPort=5055;
	private String host="localhost";
	
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		System.out.println("server context has been destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		myContext = new ClassPathXmlApplicationContext("ThanatosBean.xml");		
		factory=new ConnectionFactory();
		factory.setHost(host);
		try{
			
			Registry reg=LocateRegistry.createRegistry(targetPort);
			RmiLoginImpl login=new RmiLoginImpl();
			RmiQuoteImpl quote=new RmiQuoteImpl();
			RmiOrderImpl order=new RmiOrderImpl();
			reg.rebind("login", login);
			reg.rebind("quote", quote);
			reg.rebind("order", order);
			System.out.println("server is ready");
			setupFixConnectionToDealer();
			SessionID sessionID = new SessionID(FIX_VERSION,SENDER,TARGET);
			Session.lookupSession(sessionID).logon();
			connection=factory.newConnection();
			orderListener=new MqOrderListener(connection,sessionID);
			System.out.println("[*] Waiting for messages. To exit press CTRL+C");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private void setupFixConnectionToDealer() throws ConfigError, SessionNotFound{
		FixClient client=new FixClient();
		SessionSettings settings = new SessionSettings("Initiator.cfg"); 
		FileStoreFactory storeFactory = new FileStoreFactory(settings); 
		ScreenLogFactory logFactory = new ScreenLogFactory(settings); 
		initiator = new SocketInitiator(client, storeFactory, settings, 
			    logFactory, new DefaultMessageFactory()); 
		initiator.start(); 
	}

}
