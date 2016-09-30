package com.thanatos.daoImpl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ThanatosServer.Utility.MyBatisUtil;
import com.thanatos.dao.QuoteDao;
import com.thanatos.rmi.intf.RmiQuote;


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
	public List<RmiQuote> getLatestQuoteInfo(String quote) {
		List<RmiQuote> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("latestQuote",quote);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
