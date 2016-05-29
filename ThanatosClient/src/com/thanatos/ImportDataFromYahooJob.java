package com.thanatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ImportDataFromYahooJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		URL u;
		InputStream is=null;
		BufferedReader dis;
		String s="";
		String[] values;
		try{
			u=new URL("http://download.finance.yahoo.com/d/quotes.csv?s=GOOG&f=sl1d1t1c1ohgv&e=.csv");
			is=u.openStream();
			dis=new BufferedReader(new InputStreamReader(is));
			while((s=dis.readLine())!=null){
				values=s.split(",");	
				System.out.println(values[0]);
			}
		}
		catch(MalformedURLException me){
			System.out.println(me.getMessage());
		}
		catch(IOException io){
			System.out.println(io.getMessage());
		}
		finally{
			try{is.close();}catch(IOException io){io.getMessage();}
		}
	}

}
