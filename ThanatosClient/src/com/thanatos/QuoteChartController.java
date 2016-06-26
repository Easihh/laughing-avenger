package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
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
import javafx.scene.control.Tooltip;

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
    private List<RmiQuote> myQuotes;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			rmi=(RmiQuoteIntf)myReg.lookup("quote");
			List<RmiQuote> myQuotes=rmi.getLast24HoursQuoteInfo("GOOG");
			Collections.sort(myQuotes);
			minMax=new MinMax(myQuotes);
			qChart.getStyleClass().add("thick-chart");
			yQuoteChartAxis.setLowerBound(minMax.getMin());
			yQuoteChartAxis.setUpperBound(minMax.getMax());
			yQuoteChartAxis.setTickUnit((minMax.getMax()-minMax.getMin())/5);
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
			xQuoteChartAxis.setLowerBound(9.0);
			xQuoteChartAxis.setUpperBound(16.0);
			xQuoteChartAxis.setTickUnit(1);
			XYChart.Series<Double,Double> series=new XYChart.Series<Double,Double>();
			double minute=0.30;
			double hours=9.0;
			double transformIntoTick=0;
			for(RmiQuote rmi:myQuotes){
				if(minute>0)
					transformIntoTick=((minute/0.6))-minute;
				series.getData().add(new XYChart.Data<Double,Double>((hours+minute+transformIntoTick), rmi.getLastPx()));
				System.out.println("Point at:"+(hours+minute+transformIntoTick));				
				minute+=0.01;		
				System.out.println("Minute:"+minute);
				if(minute >=0.60){
					minute=0;
					hours++;
					transformIntoTick=0;
				}
			}
	        qChart.getData().add(series);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshMonitor() {
		System.out.println("Refreshing Chart View");
		
	}
}
