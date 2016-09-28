package com.thanatos.rmi.intf.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.thanatos.rmi.intf.RmiOrder;

public interface RmiOrderIntf extends Remote{
	
	public List<RmiOrder> getPendingOrders()throws RemoteException;
	
}

