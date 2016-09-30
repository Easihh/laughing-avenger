package com.thanatos.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.thanatos.common.intf.RmiQuoteIntf;
import com.thanatos.common.model.RmiQuote;
import com.thanatos.server.dao.QuoteDao;
import com.thanatos.server.utility.AppUtils;

public class RmiQuoteImpl extends UnicastRemoteObject implements RmiQuoteIntf{

	private static final long serialVersionUID = 5310634897808966329L;
	private QuoteDao quoteDao;

	public RmiQuoteImpl() throws RemoteException {
		quoteDao=(QuoteDao)AppUtils.getAppContext().getBean("quoteDao");
	}

	@Override
	public List<RmiQuote> getQuotesInfo(List<String> quotes)
			throws RemoteException {
		return quoteDao.getQuotesInfo(quotes);
	}

	@Override
	public List<RmiQuote> getLatestQuoteInfo(String quote) {
		return quoteDao.getLatestQuoteInfo(quote);
	}
	
}
