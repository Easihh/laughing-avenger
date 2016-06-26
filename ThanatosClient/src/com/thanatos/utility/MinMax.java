package com.thanatos.utility;

import java.util.List;

import com.thanatos.shared.RmiQuote;

public class MinMax {
	
	private double min;
	private double max;
	
	public MinMax(List<RmiQuote> myQuotes){
		min=Integer.MAX_VALUE;
		max=Integer.MIN_VALUE;
		for(RmiQuote rmi:myQuotes){
			if(rmi.getLastPx()<min)
				min=rmi.getLastPx();
			if(rmi.getLastPx()>max)
				max=rmi.getLastPx();
		}
	}
		
	public double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
}
