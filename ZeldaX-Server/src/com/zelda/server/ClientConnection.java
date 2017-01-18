package com.zelda.server;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.zelda.common.Constants;
import com.zelda.common.network.ByteMessageReader;
import com.zelda.common.network.HeroAttackMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.MovementMessage;
import com.zelda.server.entity.Player;

public class ClientConnection {

    private Long unixTimestamp;
    private Player hero;
    private ByteMessageReader reader;
    private Map<Integer,Message> lastMessageByType;

    public ClientConnection() {
        unixTimestamp = Calendar.getInstance().getTime().getTime();
        hero = new Player();
        lastMessageByType = new HashMap<Integer, Message>();
        /**Default Value before receiving message of given type **/
        lastMessageByType.put(Constants.MessageType.MOVEMENT, new MovementMessage(""));
        //lastMessageByType.put(Constants.MessageType.HERO_MAIN_ATTACK, new HeroAttackMessage());
        reader = new ByteMessageReader();        
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
    
    public Map<Integer, Message> getLastMessageByType() {
        return lastMessageByType;
    }
}
