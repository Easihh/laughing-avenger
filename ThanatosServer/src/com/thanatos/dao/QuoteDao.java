package com.thanatos.dao;

import java.util.List;

import com.thanatos.rmi.intf.RmiQuote;



public interface QuoteDao {
	
	public List<RmiQuote> getQuotesInfo(List<String> quotes);

	public List<RmiQuote> getLatestQuoteInfo(String quote);
	
}
