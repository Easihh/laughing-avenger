package com.thanatos.server.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.common.model.RmiOrder;
import com.thanatos.server.dao.OrderDao;
import com.thanatos.server.main.OrderStatus;
import com.thanatos.server.utility.MyBatisUtil;


public class OrderDaoImpl  implements OrderDao{

	@Override
	public List<RmiOrder> getPendingOrders() {
		List<RmiOrder> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("pendingOrder",OrderStatus.PENDING.toString());
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
