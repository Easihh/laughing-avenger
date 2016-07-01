package com.thanatos;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;
import com.thanatos.utility.MinMax;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class QuoteChartController implements Initializable{
    
	@FXML
	private LineChart<Long, Double> qChart;
	@FXML
	private NumberAxis xQuoteChartAxis;
	@FXML
	private NumberAxis yQuoteChartAxis;
    private Registry myReg;
    private RmiQuoteIntf rmi;
    private MinMax minMax;
    private final String targetIp="127.0.0.1";
    private final int targetPort=5055;
    private XYChart.Series<Long,Double> series;
    private final int nbrCategoryBetweenLowerUpper=3;
    private List<RmiQuote> myQuotes;
    private final static long TWOHOURS_MILLISECONDS=7200000;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ControllerManager.setQuoteChartController(this);
		try {
			myReg=LocateRegistry.getRegistry(targetIp,targetPort);
			rmi=(RmiQuoteIntf)myReg.lookup("quote");
			setupChart();
			qChart.getStyleClass().add("thick-chart");
			yQuoteChartAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yQuoteChartAxis){
				 @Override public String toString(Number object) {
					 String formatToTwoDecimals=String.format("%.2f",object);
					 return formatToTwoDecimals; }
			 });
			xQuoteChartAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xQuoteChartAxis){
				 @Override public String toString(Number object) {
					 Long dateLong=object.longValue();
					 Calendar cal=Calendar.getInstance();
					 cal.setTimeInMillis(dateLong);
					 String amOrPm=(cal.get(Calendar.HOUR_OF_DAY)>=12 && cal.get(Calendar.HOUR_OF_DAY)<24) ? "PM":"AM";
					 return String.format("%1$s:00"+amOrPm, cal.get(Calendar.HOUR_OF_DAY)); }
			 });
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void refreshMonitor() {
		System.out.println("Refreshing Chart View");
		try{
			qChart.getData().clear();
			series.getData().clear();
			setupChart();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void addChartSeries() {
		series=new XYChart.Series<Long,Double>();
		for(RmiQuote rmi:myQuotes){	
			series.getData().add(new XYChart.Data<Long,Double>(rmi.getTimeDate().getTime(), rmi.getLastPx()));
		}
		qChart.getData().add(series);
	}
	private void setupChart() throws RemoteException{
		xQuoteChartAxis.setTickUnit(TWOHOURS_MILLISECONDS);
		myQuotes=rmi.getLast24HoursQuoteInfo("GOOG");
		Collections.sort(myQuotes);
		setupXAxis();
		minMax=new MinMax(myQuotes);
		double lowerBound=(Double.valueOf(minMax.getMin())).intValue();
		double highestBound=minMax.getMax();
		double spread=highestBound-lowerBound;
		int step=getNextAxisYStepSize(spread);
		int yTickUnit=step/nbrCategoryBetweenLowerUpper;	
		highestBound=(Double.valueOf(minMax.getMax())).intValue()+yTickUnit;
		yQuoteChartAxis.setLowerBound(lowerBound);
		yQuoteChartAxis.setUpperBound(highestBound);
		yQuoteChartAxis.setTickUnit(yTickUnit);
		addChartSeries();
	}
	private void setupXAxis(){
		Calendar lowest=Calendar.getInstance();
		lowest.setTime(myQuotes.get(0).getTimeDate());
		lowest.add(Calendar.MINUTE, -lowest.get(Calendar.MINUTE));
		xQuoteChartAxis.setLowerBound(lowest.getTimeInMillis());	
		Calendar highest=Calendar.getInstance();		
		highest.setTime(myQuotes.get(myQuotes.size()-1).getTimeDate());
		if(highest.get(Calendar.MINUTE)!=0)
			highest.add(Calendar.MINUTE, 60-highest.get(Calendar.MINUTE));
		xQuoteChartAxis.setUpperBound(highest.getTimeInMillis());
	}
	private int getNextAxisYStepSize(double spread){
		int retVal=0;
		if(spread>=1){
			retVal=Double.valueOf(spread).intValue();
			if(retVal%nbrCategoryBetweenLowerUpper!=0)
				retVal+=nbrCategoryBetweenLowerUpper-(retVal%nbrCategoryBetweenLowerUpper);
		}
		return retVal;
	}
}
