package com.thanatos.Dao;

import java.util.List;

import com.thanatos.model.Quote;

public interface QuoteDao {
	
	public void insertHistorical(String[] dataArr);
	
	public void updateCurrentQuote(String[] dataArr);
	
	public List<Quote> getWatchedQuotes();
}
