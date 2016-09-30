package com.thanatos.server.dao;

import java.util.List;

import com.thanatos.common.model.RmiQuote;


public interface QuoteDao {
	
	public List<RmiQuote> getQuotesInfo(List<String> quotes);

	public List<RmiQuote> getLatestQuoteInfo(String quote);
	
}
