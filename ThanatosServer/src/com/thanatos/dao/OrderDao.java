package com.thanatos.dao;

import java.util.List;

import com.thanatos.rmi.intf.RmiOrder;



public interface OrderDao {
	
	public List<RmiOrder> getPendingOrders();
	
}
