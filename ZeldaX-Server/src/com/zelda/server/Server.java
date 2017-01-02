package com.zelda.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.Constants;
import com.zelda.common.network.HeroIdentiferMessage;
import com.zelda.common.network.Message;
import com.zelda.server.entity.Player;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.processor.ClientMessageProcessor;
import com.zelda.server.snaphot.GameSnapshot;
import static com.zelda.common.Constants.BUFFER_READ_LENGTH;;

public class Server implements Runnable {

    private final static String HOST = "localhost";
    private final static int PORT = 1111;
    private static Selector selector;
    private ServerSocketChannel serverSocket;
    private SelectionKey myKey;
    private Set<SelectionKey> serverKeys;
    private Iterator<SelectionKey> keyIterator;
    private ClientConnection clientConnection;
    private GameData gameData;
    private ClientMessageProcessor cProcessor;
    private static boolean isRunning = true;
    private final Logger LOG = LoggerFactory.getLogger(Server.class);
    private GameSnapshot gameSnaphot;

    public Server() {
        gameData = GameData.getInstance();
        cProcessor = new ClientMessageProcessor();
        gameSnaphot = new GameSnapshot();
        Thread snaphot = new Thread(gameSnaphot);
        snaphot.start();
    }

    @Override
    public void run() {

        setupServer();

        while (isRunning) {
            selectKeys();
            while (keyIterator.hasNext()) {
                myKey = keyIterator.next();

                if (myKey.isAcceptable()) {
                    acceptConnection();
                } else if (myKey.isReadable()) {
                    readFromChannel();
                }
                keyIterator.remove();
            }
        }
    }

    private void selectKeys() {
        try {
            selector.select();
            serverKeys = selector.selectedKeys();
            keyIterator = serverKeys.iterator();
        }
        catch (IOException e) {
            LOG.warn("key selection failed." + e);
        }
    }

    private void readFromChannel() {
        LOG.debug("Reading from Channel.");
        SocketChannel client = null;
        try {
            client = (SocketChannel) myKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_READ_LENGTH);
            client.read(buffer);

            clientConnection = gameData.getActiveConnection().get(myKey);
            Queue<Message> clientMessages = new LinkedList<Message>();
            clientMessages.addAll(clientConnection.getReader().decodeMessage(buffer));
            List<ServerMessage> serverMessages = cProcessor.process(clientConnection, clientMessages);
            gameData.getGameSimulationMessageQueue().addAll(serverMessages);
        }
        catch (Exception e) {
            LOG.warn("Connection to client lost. " + e);
            disconnectClient(client);
        }
    }

    private void disconnectClient(SocketChannel client) {
        /** Player Character to be removed from the game **/
        String EntityKey = gameData.getActiveConnection().get(myKey).getHero().getFullIdentifier();
        Player playerHero = (Player) gameData.getGameEntityMap().get(EntityKey);
        playerHero.setObjState(Constants.ObjectState.INACTIVE);
        gameSnaphot.getsAction().sendUpdateToAllPlayers();
        gameData.getActiveConnection().remove(myKey);
        try {
            client.close();
        }
        catch (IOException e1) {
            LOG.error("Failed to close client connection. " + e1);
        }
    }

    private void acceptConnection() {
        try {
            SocketChannel zeldaXClient = serverSocket.accept();
            zeldaXClient.configureBlocking(false);

            SelectionKey clientKey = zeldaXClient.register(selector, SelectionKey.OP_WRITE);
            clientConnection = new ClientConnection();
            gameData.getActiveConnection().put(clientKey, clientConnection);
            
            Player playerHero=clientConnection.getHero();
            String entityKey = playerHero.getTypeIdenfitier() + playerHero.getIdIdentifier();
            gameData.getGameEntityMap().put(entityKey, playerHero);
            
            Message message = new HeroIdentiferMessage(playerHero.getTypeIdenfitier(), playerHero.getIdIdentifier());
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            zeldaXClient.write(buffer);
            LOG.debug("Sending:" + clientConnection.getHero().getTypeIdenfitier()
                            + clientConnection.getHero().getIdIdentifier());

            // force send current world state here.
            gameSnaphot.getsAction().sendSnapshotToCurrentPlayer(clientKey);

            clientKey.interestOps(SelectionKey.OP_READ);
            LOG.info("Connection Accepted: " + zeldaXClient.getLocalAddress() + "\n");

        }
        catch (Exception e) {
            LOG.error("Failed to Accept client Connection.", e);
        }
    }

    private void setupServer() {
        try {
            selector = Selector.open();

            serverSocket = ServerSocketChannel.open();
            InetSocketAddress serverAddr = new InetSocketAddress(HOST, PORT);

            serverSocket.bind(serverAddr);

            serverSocket.configureBlocking(false);

            int ops = serverSocket.validOps();
            serverSocket.register(selector, ops, null);

        }
        catch (Exception e) {
            LOG.error("failed to setup Server." + e);
        }
        LOG.info("Server started.");
    }
}
