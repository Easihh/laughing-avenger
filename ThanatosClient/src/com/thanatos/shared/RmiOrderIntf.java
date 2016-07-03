package com.thanatos.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiOrderIntf extends Remote{
	
	public List<RmiOrder> getPendingOrders(List<String> quotes)throws RemoteException;
	
}

