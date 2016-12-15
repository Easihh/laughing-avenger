package com.zelda.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer {

    protected boolean isConnected = true;
    protected SocketChannel channel;
    private Logger LOG = LoggerFactory.getLogger(AbstractServer.class);

    protected AbstractServer(SocketChannel channel) {
        this.channel = channel;
        isConnected = true;
    }

    protected void disconnectFromServer() {
        try {
            channel.close();
            isConnected = false;
        }
        catch (IOException e) {
            LOG.error("Failed to disconnect from Server." + e);
        }
    }
}
