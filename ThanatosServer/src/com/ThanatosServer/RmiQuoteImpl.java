package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.ThanatosServer.Dao.QuoteDao;
import com.ThanatosServer.Dao.UserDao;
import com.ThanatosServer.Utility.AppUtils;
import com.thanatos.shared.RmiLoginIntf;
import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

public class RmiQuoteImpl extends UnicastRemoteObject implements RmiQuoteIntf{

	private static final long serialVersionUID = 1647821219723890263L;
	private QuoteDao quoteDao;

	public RmiQuoteImpl() throws RemoteException {
		quoteDao=(QuoteDao)AppUtils.getAppContext().getBean("quoteDao");
		quoteDao.getLast24HoursQuoteInfo("GOOG");
	}

	@Override
	public List<RmiQuote> getQuotesInfo(List<String> quotes)
			throws RemoteException {
		return quoteDao.getQuotesInfo(quotes);
	}

	@Override
	public List<RmiQuote> getLast24HoursQuoteInfo(String quote) {
		return quoteDao.getLast24HoursQuoteInfo(quote);
	}
	
}