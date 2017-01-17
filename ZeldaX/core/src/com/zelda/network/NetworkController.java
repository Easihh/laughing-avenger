package com.zelda.network;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.network.Message;

public class NetworkController {

    private final static int PORT = 1111;
    private final static String HOST = "184.161.178.153";
    private ServerReader serverReader;
    private ServerWriter serverWriter;
    
    private Logger LOG = LoggerFactory.getLogger(NetworkController.class);

    public NetworkController(Queue<Message> fromServerMessageQueue) {
        SocketChannel channel = startConnection();
        if (channel != null) {
            serverReader = new ServerReader(channel,fromServerMessageQueue);
            serverWriter = new ServerWriter(channel);
            Thread sr = new Thread(serverReader);
            Thread sw = new Thread(serverWriter);
            sr.start();
            sw.start();
        }
    }

    private SocketChannel startConnection() {
        LOG.info("Connecting to " + HOST + " on port " + PORT + "...");
        SocketChannel channel = null;
        try {
            InetSocketAddress serverAddr = new InetSocketAddress(HOST, PORT);
            channel = SocketChannel.open(serverAddr);
            while (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            LOG.info("Connection success.");
        }
        catch (Exception e) {
            LOG.error("Connection to Server failed." + e);
        }
        return channel;
    }
}
