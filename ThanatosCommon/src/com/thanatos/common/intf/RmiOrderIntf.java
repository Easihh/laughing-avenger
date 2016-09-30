package com.thanatos.common.intf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.thanatos.common.model.RmiOrder;

public interface RmiOrderIntf extends Remote{
	
	public List<RmiOrder> getPendingOrders()throws RemoteException;
	
}

