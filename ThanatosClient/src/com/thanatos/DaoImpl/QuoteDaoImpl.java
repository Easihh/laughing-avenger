package com.thanatos.DaoImpl;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.Dao.OrderDao;
import com.thanatos.Dao.QuoteDao;
import com.thanatos.model.Order;
import com.thanatos.model.Quote;
import com.thanatos.utility.MyBatisUtil;

public class QuoteDaoImpl  implements QuoteDao{

	@Override
	public void insert(String[] toBeInserted) {
		Quote quote=new Quote(toBeInserted);
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			session.insert("insertQuote",quote);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
