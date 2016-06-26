package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

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
    private Registry myReg;
    private RmiQuoteIntf rmi;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			rmi=(RmiQuoteIntf)myReg.lookup("quote");
			List<RmiQuote> myquote=rmi.getLast24HoursQuoteInfo("GOOG");
			qChart.getStyleClass().add("thick-chart");
			xQuoteChartAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xQuoteChartAxis){
				 @Override public String toString(Number object) { 
					 String removeDecimal=Integer.toString(object.intValue());
					 String amOrPm=(object.intValue()>=12 && object.intValue()<24) ? "PM":"AM";
					 return String.format("%1$s:00"+amOrPm, removeDecimal); }
			 });
			XYChart.Series<Double,Double> series=new XYChart.Series<Double,Double>();
			series.getData().add(new XYChart.Data<Double,Double>(10.0, 23.0));
			series.getData().add(new XYChart.Data<Double,Double>(10.1, 21.0));
	        series.getData().add(new XYChart.Data<Double,Double>(10.2, 14.0));
	        series.getData().add(new XYChart.Data<Double,Double>(11.0, 15.0));
	        series.getData().add(new XYChart.Data<Double,Double>(12.0, 24.0));
	        series.getData().add(new XYChart.Data<Double,Double>(12.30, 34.0));
	        series.getData().add(new XYChart.Data<Double,Double>(16.00, 22.0));
	        qChart.getData().add(series);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshMonitor() {
		System.out.println("Refreshing Chart View");
		
	}
}
