package com.zelda.network;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NetworkController {
    
    private final static int PORT = 1111;
    private final static String HOST = "localhost";
    private ServerReader serverReader;
    private ServerWriter serverWriter;
   

    public NetworkController() {
        SocketChannel channel = startConnection();
        if (channel != null) {
            serverReader = new ServerReader(channel);
            serverWriter = new ServerWriter(channel);
            Thread sr = new Thread(serverReader);
            Thread sw = new Thread(serverWriter);
            sr.start();
            sw.start();
        }
    }
    
    private SocketChannel startConnection() {
        System.out.println("Connecting to " + HOST + " on port " + PORT + "...");
        SocketChannel channel = null;
        try {
            InetSocketAddress serverAddr = new InetSocketAddress(HOST, PORT);
            channel = SocketChannel.open(serverAddr);
            while (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            System.out.println("Connection success.");
        }
        catch (Exception e) {
            System.out.println("Connection to Server failed." + e);
        }
        return channel;
    }
}
