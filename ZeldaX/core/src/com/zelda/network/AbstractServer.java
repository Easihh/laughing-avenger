package com.zelda.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class AbstractServer {
    
    protected boolean isConnected = true;
    protected SocketChannel channel;
    
    protected AbstractServer(SocketChannel channel){
        this.channel = channel;
        isConnected = true;
    }
    
    protected void disconnectFromServer() {
        try {
            channel.close();
            isConnected = false;
        }
        catch (IOException e) {
            System.out.println("Failed to disconnect from Server." + e);
        }
    }
}
