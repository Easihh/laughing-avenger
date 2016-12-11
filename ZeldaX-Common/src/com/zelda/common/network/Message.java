package com.zelda.common.network;

public interface Message {
    
    public byte[] getBytes();

    public int getTotalLength();

    public int getType();
}
