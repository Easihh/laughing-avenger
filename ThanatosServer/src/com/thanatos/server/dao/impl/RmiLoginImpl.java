package com.thanatos.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.thanatos.common.intf.RmiLoginIntf;
import com.thanatos.server.dao.UserDao;
import com.thanatos.server.utility.AppUtils;

public class RmiLoginImpl extends UnicastRemoteObject implements RmiLoginIntf{

	private static final long serialVersionUID = -6129122246141857484L;
	
	private UserDao userDao;
	
	public RmiLoginImpl() throws RemoteException {
		userDao=(UserDao)AppUtils.getAppContext().getBean("userDao");
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
