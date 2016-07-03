package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.ThanatosServer.Dao.QuoteDao;
import com.ThanatosServer.Utility.AppUtils;
import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

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
