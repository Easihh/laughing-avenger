package com.thanatos.DaoImpl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.Dao.QuoteDao;
import com.thanatos.model.Quote;
import com.thanatos.utility.MyBatisUtil;

public class QuoteDaoImpl  implements QuoteDao{

	@Override
	public void insertHistorical(String[] toBeInserted) {
		Quote quote=new Quote(toBeInserted);
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			session.insert("insertHistoricalQuote",quote);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void updateCurrentQuote(String[] toBeInserted) {
		Quote quote=new Quote(toBeInserted);
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			session.update("updateQuote",quote);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<Quote> getWatchedQuotes() {
		List<Quote> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("selectQuotes");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
