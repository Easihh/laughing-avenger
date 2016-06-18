package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.thanatos.shared.RmiLoginIntf;

public class RmiLoginImpl extends UnicastRemoteObject implements RmiLoginIntf{

	private static final long serialVersionUID = -6129122246141857484L;

	public RmiLoginImpl() throws RemoteException {}

	@Override
	public boolean heartbeat() throws RemoteException {
		return true;
	}

	@Override
	public boolean login(String username, String password)
			throws RemoteException {
		
		return true;
	}
	
}
