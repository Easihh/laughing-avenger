package com.zelda.server.snaphot;

import com.zelda.server.message.processor.ServerMessageProcessor;

public class GameSnapshot implements Runnable {

    private boolean isRunning = true;
    private static int FPS = 10;
    private static long NANO = 1000000000L;
    private static long TIME_BETWEEN_RENDER = NANO / (long) FPS;
    private static long ONE_MILLIS = 1000L;
    private static long ONE_MILLION = 1000000L;
    private SnapshotAction sAction;
    private ServerMessageProcessor sProcessor;

    public GameSnapshot() {
        sAction = new SnapshotAction();
        sProcessor = new ServerMessageProcessor();
    }

    @Override
    public void run() {
        long lastFpsUpdate = System.nanoTime();
        while (isRunning) {
            long lastUpdate = System.nanoTime();
            sProcessor.process();
            sAction.sendUpdateToAllPlayers();
            if (lastUpdate - System.nanoTime() < TIME_BETWEEN_RENDER) {
                long timeToNextUpdate = TIME_BETWEEN_RENDER - (lastUpdate - System.nanoTime());
                try {
                    Thread.sleep(timeToNextUpdate / ONE_MILLION);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if ((System.nanoTime() - lastFpsUpdate) / ONE_MILLION >= ONE_MILLIS) {
                lastFpsUpdate = System.nanoTime();
            }
        }
    }

    public SnapshotAction getsAction() {
        return sAction;
    }
}
