package com.ThanatosServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ApplicationListener implements ServletContextListener {
	
	private MqOrderListener orderListener;
	private ConnectionFactory factory;
	private Connection connection;
	private SocketInitiator initiator;
	private static final String SENDER="CLIENT1";
	private static final String TARGET="FixDealer";
	private static final String FIX_VERSION="FIX.4.4";
	
	@Override
	public void contextDestroyed(ServletContextEvent context) {}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		System.out.println("I WAS INITIALIZED");
		factory=new ConnectionFactory();
		factory.setHost("localhost");
		try{
			
			Registry reg=LocateRegistry.createRegistry(5055);
			RmiLoginImpl add=new RmiLoginImpl();
			reg.rebind("login", add);
			System.out.println("server is ready");
			//setupFixConnectionToDealer();
			connection=factory.newConnection();
			orderListener=new MqOrderListener(connection);
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
		SessionID sessionID = new SessionID(FIX_VERSION,SENDER,TARGET);
		Session.lookupSession(sessionID).logon();
		NewOrderSingle order = new NewOrderSingle();
		order.set(new HandlInst(HandlInst.MANUAL_ORDER));
		order.set(new ClOrdID("DLF")); 
		order.set(new Symbol("DLF")); 
	    order.set(new Side(Side.BUY)); 
	    order.set(new TransactTime(new Date()));
	    order.set(new OrdType(OrdType.LIMIT)); 
		order.set(new OrderQty(45)); 
		order.set(new Price(25.4d)); 
		Session.sendToTarget(order, sessionID);
	}

}
