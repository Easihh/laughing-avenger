package com.ThanatosServer.Dao;

import java.util.List;

import com.thanatos.shared.RmiOrder;
import com.thanatos.shared.RmiQuote;


public interface OrderDao {
	
	public List<RmiOrder> getPendingOrders();
	
}
