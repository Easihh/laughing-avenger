package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.ThanatosServer.Dao.OrderDao;
import com.ThanatosServer.Utility.AppUtils;
import com.thanatos.shared.RmiOrder;
import com.thanatos.shared.RmiOrderIntf;

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
