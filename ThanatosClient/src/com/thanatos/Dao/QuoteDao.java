package com.thanatos.Dao;

import java.util.List;

import com.thanatos.model.Quote;

public interface QuoteDao {
	
	public List<Quote> getWatchedQuotes();
}
