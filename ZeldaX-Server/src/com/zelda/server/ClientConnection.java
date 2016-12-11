package com.zelda.server;

import java.util.Calendar;

import com.zelda.common.network.ByteMessageReader;
import com.zelda.server.entity.Player;

public class ClientConnection {

    private Long unixTimestamp;
    private Player hero;
    private ByteMessageReader reader;

    public ClientConnection() {
        hero = new Player();
        reader = new ByteMessageReader();
        unixTimestamp = Calendar.getInstance().getTime().getTime();
    }

    public Player getHero() {
        return hero;
    }

    public ByteMessageReader getReader() {
        return reader;
    }

    @Override
    public int hashCode() {
        int hashCode = Long.valueOf((unixTimestamp * 31) * 31).hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        ClientConnection target = (ClientConnection) obj;
        return unixTimestamp.longValue() == target.unixTimestamp.longValue();
    }

    public Long getUnixTimestamp() {
        return unixTimestamp;
    }
}
