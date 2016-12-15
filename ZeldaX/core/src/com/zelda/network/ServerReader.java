package com.zelda.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.network.ByteMessageReader;
import com.zelda.common.network.Message;
import com.zelda.common.Constants;

public class ServerReader extends AbstractServer implements Runnable {

    private ByteMessageReader reader;
    private Logger LOG = LoggerFactory.getLogger(ServerWriter.class);

    private volatile Queue<Message> serverMessageQueue;

    public ServerReader(SocketChannel channel, Queue<Message> fromServerMessageQueue) {
        super(channel);
        serverMessageQueue = fromServerMessageQueue;
    }

    @Override
    public void run() {
        reader = new ByteMessageReader();
        LOG.info("Server Reader started.");
        while (isConnected) {
            ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_READ_LENGTH);
            try {
                channel.read(buffer);
                List<Message> msgList = reader.decodeMessage(buffer);
                serverMessageQueue.addAll(msgList);
            }
            catch (IOException e) {
                disconnectFromServer();
                LOG.error("Error: " + e);
            }
        }
    }
}
