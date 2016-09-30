package com.thanatos.daoImpl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ThanatosServer.Utility.MyBatisUtil;
import com.thanatos.dao.OrderDao;
import com.thanatos.main.OrderStatus;
import com.thanatos.rmi.intf.RmiOrder;


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
