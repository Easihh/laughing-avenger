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
	private LineChart<Double, Double> qChart;
	@FXML
	private NumberAxis xQuoteChartAxis;
	@FXML
	private NumberAxis yQuoteChartAxis;
    private Registry myReg;
    private RmiQuoteIntf rmi;
    private MinMax minMax;
    private final String targetIp="127.0.0.1";
    private final int targetPort=5055;
    private XYChart.Series<Double,Double> series;
    private final int numberOfYaxisCategory=5;
    private List<RmiQuote> myQuotes;
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
					 String removeDecimal=Integer.toString(object.intValue());
					 String amOrPm=(object.intValue()>=12 && object.intValue()<24) ? "PM":"AM";
					 return String.format("%1$s:00"+amOrPm, removeDecimal); }
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

	private void buildChartAxis(Calendar earliestPoint) {
		double hours=earliestPoint.get(Calendar.HOUR_OF_DAY);
		double minute=earliestPoint.get(Calendar.MINUTE)/100.0;
		double transformIntoTick=0;
		final double ratioToPoint=0.6;//100points between two decimal in chart but only 60mins in an hour
		series=new XYChart.Series<Double,Double>();
		for(RmiQuote rmi:myQuotes){
			if(minute>0)
				transformIntoTick=((minute/ratioToPoint))-minute;			
			series.getData().add(new XYChart.Data<Double,Double>((hours+minute+transformIntoTick), rmi.getLastPx()));
			System.out.println("Point at:"+(hours+minute+transformIntoTick)+" data:"+rmi.getLastPx());	
			minute+=0.01;		
			if(minute >=0.60){
				minute=0;
				hours++;
				transformIntoTick=0;
			}
		}
		qChart.getData().add(series);
	}
	private void setupChart() throws RemoteException{
		xQuoteChartAxis.setTickUnit(1);
		myQuotes=rmi.getLast24HoursQuoteInfo("GOOG");
		Collections.sort(myQuotes);
		Calendar lowest=Calendar.getInstance();
		lowest.setTime(myQuotes.get(0).getTimeDate());
		xQuoteChartAxis.setLowerBound(lowest.get(Calendar.HOUR_OF_DAY));	
		Calendar highest=Calendar.getInstance();		
		highest.setTime(myQuotes.get(myQuotes.size()-1).getTimeDate());
		xQuoteChartAxis.setUpperBound(highest.get(Calendar.HOUR_OF_DAY));
		//setup Y Axis
		minMax=new MinMax(myQuotes);
		yQuoteChartAxis.setLowerBound(minMax.getMin());
		yQuoteChartAxis.setUpperBound(minMax.getMax());
		yQuoteChartAxis.setTickUnit((minMax.getMax()-minMax.getMin())/numberOfYaxisCategory);
		buildChartAxis(lowest);
	}
}
