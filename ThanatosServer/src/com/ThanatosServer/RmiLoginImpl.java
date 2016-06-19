package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.ThanatosServer.Dao.UserDao;
import com.thanatos.shared.RmiLoginIntf;

public class RmiLoginImpl extends UnicastRemoteObject implements RmiLoginIntf{

	private static final long serialVersionUID = -6129122246141857484L;
	
	private UserDao userDao;

	public RmiLoginImpl() throws RemoteException {
		userDao=(UserDao)ApplicationListener.myContext.getBean("userDao");
	}

	@Override
	public boolean heartbeat() throws RemoteException {
		return true;
	}

	@Override
	public boolean login(String username, String password)
			throws RemoteException {
		
		if(username.equals("") || password.equals(""))
			return false;
		return userDao.getUserByUsernamePassword(username, password);
	}
	
}
