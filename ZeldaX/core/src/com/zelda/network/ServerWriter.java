package com.zelda.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.network.ByteMessageReader;
import com.zelda.common.network.Message;

public class ServerWriter extends AbstractServer implements Runnable {

    private static BlockingQueue<byte[]> messageQueue;
    private static Logger LOG = LoggerFactory.getLogger(ServerWriter.class);

    public ServerWriter(SocketChannel channel) {
        super(channel);
        messageQueue = new LinkedBlockingQueue<byte[]>();
        new ByteMessageReader();
    }

    @Override
    public void run() {
        LOG.info("Server Writer started.");
        while (isConnected) {
            try {
                byte[] message = messageQueue.take();
                ByteBuffer buffer = ByteBuffer.wrap(message);
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

    public static void sendMessage(Message msg) {
        try {
            messageQueue.put(msg.getBytes());
        }
        catch (InterruptedException e) {
            LOG.error("Error adding message to Queue." + e);
        }
    }
    
    public static void sendBulkMessage(List<Message> msgList) {
        byte[] byteArr = null;
        for (Message msg : msgList) {
            byte[] tempArr = msg.getBytes();
            byteArr = ArrayUtils.addAll(byteArr, tempArr);
        }
        try {
            messageQueue.put(byteArr);
        }
        catch (InterruptedException e) {
            LOG.error("Error adding message to Queue." + e);
        }
    }
}
