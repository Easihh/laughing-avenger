package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;

public class QuoteChartController implements Initializable{
    
	@FXML
	private LineChart<String, Double> qChart;
	@FXML
	private ScrollPane chartScrollPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//chartScrollPane.setPrefWidth(1000);
		XYChart.Series<String,Double> series=new XYChart.Series<String,Double>();
        //populating the series with data
		series.getData().add(new XYChart.Data<String,Double>("1", 23.0));;
        series.getData().add(new XYChart.Data<String,Double>("2", 14.0));
        series.getData().add(new XYChart.Data<String,Double>("3", 15.0));
        series.getData().add(new XYChart.Data<String,Double>("4", 24.0));
        series.getData().add(new XYChart.Data<String,Double>("5", 34.0));
        series.getData().add(new XYChart.Data<String,Double>("6", 36.0));
        series.getData().add(new XYChart.Data<String,Double>("7", 22.0));
		series.getData().add(new XYChart.Data<String,Double>("8", 23.0));;
        series.getData().add(new XYChart.Data<String,Double>("9", 14.0));
        series.getData().add(new XYChart.Data<String,Double>("10", 15.0));
        series.getData().add(new XYChart.Data<String,Double>("11", 24.0));
        series.getData().add(new XYChart.Data<String,Double>("12", 34.0));
        series.getData().add(new XYChart.Data<String,Double>("13", 36.0));
        series.getData().add(new XYChart.Data<String,Double>("14", 22.0));
        
        qChart.getData().add(series);
	}

	public void refreshMonitor() {
		System.out.println("Refreshing Chart View");
	}
}
