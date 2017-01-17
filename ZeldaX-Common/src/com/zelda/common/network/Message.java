package com.zelda.common.network;

public abstract class Message {
    
    protected Long timeCreated;
    
    public Long getTimeCreated() {
        return timeCreated;
    }

    public abstract byte[] getBytes();

    public abstract int getTotalLength();

    public abstract int getType();
}
