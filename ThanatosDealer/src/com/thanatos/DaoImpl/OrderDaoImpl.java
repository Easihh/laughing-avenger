package com.thanatos.DaoImpl;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.Dao.OrderDao;
import com.thanatos.model.Order;
import com.thanatos.utility.MyBatisUtil;

public class OrderDaoImpl  implements OrderDao{

	@Override
	public void insert(Order toBeInserted) {
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			session.insert("insertOrder",toBeInserted);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
