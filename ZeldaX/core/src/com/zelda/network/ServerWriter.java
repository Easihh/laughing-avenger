package com.zelda.network;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.zelda.common.Constants;

public class ServerWriter extends AbstractServer implements Runnable {

    private String msg;
    private static BlockingQueue<String> messageQueue;
    
    public ServerWriter(SocketChannel channel) {
        super(channel);
        messageQueue=new LinkedBlockingQueue<String>();
    }

    @Override
    public void run() {
        int bytes = -1;
        System.out.println("Server Writer started.");
        while (isConnected) {
            try {
                msg=messageQueue.take();
                byte[] message=messageToBytes();
                ByteBuffer buffer=ByteBuffer.wrap(message);
                System.out.println("Message is:"+msg);
                while(buffer.hasRemaining()){
                    bytes = channel.write(buffer); //will block if buffer full;
                }
            }
            catch (IOException e) {
                disconnectFromServer();
                System.out.println("Failed to write to Channel." + e);
            }
            catch (InterruptedException e) {
                System.out.println("ServerReader thread was interupted while waiting." + e); 
                e.printStackTrace();
            }
        }
    }
    
    private byte[] messageToBytes() {
        int messageType=Integer.valueOf(msg.substring(0,4));
        int messageLength=1;
        
        String value=msg.substring(4,msg.length());
        ByteBuffer buffer=ByteBuffer.allocate(Constants.MessageType.LENGTH+messageLength);
        
        buffer.putInt(messageType);
        buffer.put(value.getBytes());
        //System.out.println(Calendar.getInstance().getTimeInMillis());
        //buffer.putLong(Calendar.getInstance().getTimeInMillis());
        buffer.flip();
        return buffer.array();
    }

    public static void sendMessage(String msg) {
        try {
            messageQueue.put(msg);
        }
        catch (InterruptedException e) {
            System.out.println("Error adding message to Queue." + e);
        }
    }
}
