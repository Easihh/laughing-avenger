package com.ThanatosServer.DaoImpl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ThanatosServer.Dao.QuoteDao;
import com.ThanatosServer.Utility.MyBatisUtil;
import com.thanatos.shared.RmiQuote;


public class QuoteDaoImpl  implements QuoteDao{

	@Override
	public List<RmiQuote> getQuotesInfo(List<String> quotes) {
		List<RmiQuote> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("quoteInfo",quotes);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

	@Override
	public List<RmiQuote> getLast24HoursQuoteInfo(String quote) {
		List<RmiQuote> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("last24HoursQuote",quote);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
