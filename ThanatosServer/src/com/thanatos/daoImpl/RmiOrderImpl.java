package com.thanatos.daoImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.ThanatosServer.Utility.AppUtils;
import com.thanatos.dao.OrderDao;
import com.thanatos.rmi.intf.RmiOrder;
import com.thanatos.rmi.intf.impl.RmiOrderIntf;

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
