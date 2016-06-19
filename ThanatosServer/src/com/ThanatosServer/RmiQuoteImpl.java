package com.ThanatosServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.ThanatosServer.Dao.QuoteDao;
import com.ThanatosServer.Dao.UserDao;
import com.thanatos.shared.RmiLoginIntf;
import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

public class RmiQuoteImpl extends UnicastRemoteObject implements RmiQuoteIntf{

	private static final long serialVersionUID = 1647821219723890263L;
	private QuoteDao quoteDao;

	public RmiQuoteImpl() throws RemoteException {
		quoteDao=(QuoteDao)ApplicationListener.myContext.getBean("quoteDao");
	}

	@Override
	public List<RmiQuote> getQuotesInfo(List<String> quotes)
			throws RemoteException {
		return quoteDao.getQuotesInfo(quotes);
	}
	
}
