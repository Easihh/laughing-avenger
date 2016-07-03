package com.ThanatosServer;

public enum OrderStatus {
	PENDING(1);
    private final int id;
    OrderStatus(int id) { this.id = id; }
    public int getValue() { return id; }

}
