package com.ThanatosServer.Dao;

import java.util.List;

import com.thanatos.shared.RmiQuote;


public interface QuoteDao {
	
	public List<RmiQuote> getQuotesInfo(List<String> quotes);
	
}
