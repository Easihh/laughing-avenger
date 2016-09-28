package com.thanatos.rmi.intf.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiLoginIntf extends Remote{
	
	public boolean heartbeat()throws RemoteException;
	
	public boolean login(String username,String password) throws RemoteException;
}
