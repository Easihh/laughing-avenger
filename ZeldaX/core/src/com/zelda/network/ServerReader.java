package com.zelda.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.zelda.common.network.ByteMessageReader;
import com.zelda.common.Constants;
import com.zelda.game.Game;

public class ServerReader extends AbstractServer implements Runnable {
    
    private ByteMessageReader reader;
    
    public ServerReader(SocketChannel channel) {
        super(channel);       
    }

    @Override
    public void run() {
        reader=new ByteMessageReader();
        System.out.println("Server Reader started.");
        while (isConnected) {
            ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_READ_LENGTH);
            try {
                channel.read(buffer);
                Game.fromServerMessageQueue.addAll(reader.decodeMessage(buffer));        
            }
            catch (IOException e) {
                e.printStackTrace();
                disconnectFromServer();
                System.out.println("Error: " + e);
            }
        }
    }
}
