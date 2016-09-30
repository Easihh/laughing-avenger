package com.thanatos.server.dao;

import java.util.List;

import com.thanatos.common.model.RmiOrder;



public interface OrderDao {
	
	public List<RmiOrder> getPendingOrders();
	
}
