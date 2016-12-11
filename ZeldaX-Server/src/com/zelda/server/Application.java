package com.zelda.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
@WebListener
public class Application implements ServletContextListener {

    private Server server;
    private Thread t;
    private final Logger LOG = LoggerFactory.getLogger(Application.class);

    public void contextDestroyed(ServletContextEvent context) {
        LOG.info("server context has been destroyed");
    }

    public void contextInitialized(ServletContextEvent context) {
        LOG.info("Server context has been created.");
        t = new Thread(new Server());
        t.start();
    }
}
