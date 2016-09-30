package com.thanatos.server.utility;

import java.util.Date;

import com.thanatos.common.model.RemoteOrder;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

public class TranslatorToFix {
	
	public TranslatorToFix(RemoteOrder myOrder, SessionID sessionId){
		NewOrderSingle order = new NewOrderSingle();
		order.set(new HandlInst(HandlInst.MANUAL_ORDER));
		order.set(new ClOrdID("DLF")); 
		order.set(new Symbol("DLF")); 
	    order.set(new Side(Side.BUY)); 
	    order.set(new TransactTime(new Date()));
	    order.set(new OrdType(OrdType.LIMIT)); 
		order.set(new OrderQty(45));
		order.set(new Price(25.4d)); 
		try {
				Session.sendToTarget(order, sessionId);
			} 
		catch (SessionNotFound e) {
			e.printStackTrace();
		}
	}
}
