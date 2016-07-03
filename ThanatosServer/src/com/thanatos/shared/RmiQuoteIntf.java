package com.thanatos.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiQuoteIntf extends Remote{
	
	public List<RmiQuote> getQuotesInfo(List<String> quotes)throws RemoteException;

	public List<RmiQuote> getLatestQuoteInfo(String string)throws RemoteException;;
	
}
