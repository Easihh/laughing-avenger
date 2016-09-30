package com.thanatos.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.thanatos.common.intf.RmiOrderIntf;
import com.thanatos.common.model.RmiOrder;
import com.thanatos.server.dao.OrderDao;
import com.thanatos.server.utility.AppUtils;

public class RmiOrderImpl extends UnicastRemoteObject implements RmiOrderIntf{

	private static final long serialVersionUID = 1647821219723890263L;
	private OrderDao orderDao;

	public RmiOrderImpl() throws RemoteException {
		orderDao=(OrderDao)AppUtils.getAppContext().getBean("orderDao");
	}

	@Override
	public List<RmiOrder> getPendingOrders()
			throws RemoteException {
		return orderDao.getPendingOrders();
	}
	
}
