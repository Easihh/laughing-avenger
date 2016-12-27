package com.zelda.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.Constants;

public class ServerWriter extends AbstractServer implements Runnable {

    private String msg;
    private static BlockingQueue<String> messageQueue;

    private static Logger LOG = LoggerFactory.getLogger(ServerWriter.class);

    public ServerWriter(SocketChannel channel) {
        super(channel);
        messageQueue = new LinkedBlockingQueue<String>();
    }

    @Override
    public void run() {
        LOG.info("Server Writer started.");
        while (isConnected) {
            try {
                msg = messageQueue.take();
                byte[] message = messageToBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                LOG.trace("Message is:" + msg);
                while (buffer.hasRemaining()) {
                    channel.write(buffer); // will block if buffer full;
                }
            }
            catch (IOException e) {
                disconnectFromServer();
                LOG.error("Failed to write to Channel." + e);
            }
            catch (InterruptedException e) {
                LOG.error("ServerReader thread was interupted while waiting." + e);
            }
        }
    }

    private byte[] messageToBytes() {
        int messageType = Integer.valueOf(msg.substring(0, 4));
        int messageLength = 1;

        String value = msg.substring(4, msg.length());
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MessageType.LENGTH + messageLength);

        buffer.putInt(messageType);
        buffer.put(value.getBytes());
        buffer.flip();
        return buffer.array();
    }

    public static void sendMessage(String msg) {
        try {
            messageQueue.put(msg);
        }
        catch (InterruptedException e) {
            LOG.error("Error adding message to Queue." + e);
        }
    }
}
