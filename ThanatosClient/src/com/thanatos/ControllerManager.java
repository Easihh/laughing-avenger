package com.thanatos;

public abstract class ControllerManager {
	
	private static OpenOrderController ocontroller;
	private static PendingController pcontroller;
	private static QuoteController qcontroller;
	private static QuoteChartController qccontroller;
	
	public static void setOpenOrderController(OpenOrderController ctrl){
		ocontroller=ctrl;;
	}
	
	public static OpenOrderController getOpenOrderController(){
		return ocontroller;
	}
	
	public static void setPendingController(PendingController ctrl){
		pcontroller=ctrl;;
	}
	
	public static PendingController getPendingController(){
		return pcontroller;
	}
	
	public static void setQuoteController(QuoteController ctrl){
		qcontroller=ctrl;;
	}
	
	public static QuoteController getQuoteController(){
		return qcontroller;
	}
	
	public static QuoteChartController getQuoteChartController(){
		return qccontroller;
	}

	public static void setQuoteChartController(QuoteChartController quoteChartController) {
		qccontroller=quoteChartController;
	}
}
