package com.thanatos;
	

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import quickfix.Acceptor;
import quickfix.DefaultMessageFactory;
import quickfix.Dictionary;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

public class Main{
	private Scheduler yahooScheduler;
	public static ApplicationContext ctx;
	
	public static void main(String[] args) {
		new Main().start();
	}
	
	public void start() {
		try {
			setupJobs();
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
			SessionSettings settings=new SessionSettings();
			Dictionary d=new Dictionary();
			d.setString("SocketReuseAddress", "Y");
            d.setString("ConnectionType", "acceptor");
            d.setString("SocketAcceptPort", "5001");
            d.setString("FileLogPath", "c:\\fixlog");
            d.setString("StartTime", "00:00:00");
            d.setString("EndTime", "00:00:00");
            d.setString("FileStorePath", "c:\\fixfiles");
            d.setString("UseDataDictionary", "Y");
            d.setString("DataDictionary", "FIX44.xml");
			d.setString("BeginString", "FIX.4.4");
			SessionID session=new SessionID("FIX.4.4","FixServer","CLIENT1");
	        settings.set(session,d);
			FixServer server=new FixServer();
			FileStoreFactory storeFactory=new FileStoreFactory(settings);
			ScreenLogFactory logFactory=new ScreenLogFactory(settings);
			Acceptor acceptor = new SocketAcceptor
					      (server, storeFactory, settings, logFactory, new DefaultMessageFactory());
			acceptor.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupJobs() throws SchedulerException {
		JobDetail yahooDataStart=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooStart","group1").build();
		JobDetail yahooDataDay=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooDay","group1").build();
		JobDetail yahooDataEndDay=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooEndDay","group1").build();
		Trigger yahooTrigStartDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerStart","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 46-59/1 9 ? * MON-FRI")).build();
		Trigger yahooTrigDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerDay","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * 10-15 ? * MON-FRI")).build();
		Trigger yahooTrigEndDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerEndDay","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0-16/1 16 ? * MON-FRI")).build();
		yahooScheduler=new StdSchedulerFactory().getScheduler();
		yahooScheduler.start();
		yahooScheduler.scheduleJob(yahooDataStart, yahooTrigStartDay);
		yahooScheduler.scheduleJob(yahooDataDay, yahooTrigDay);
		yahooScheduler.scheduleJob(yahooDataEndDay, yahooTrigEndDay);
	}
	private void setupTestJobs()throws SchedulerException {
		JobDetail yahooDataTest=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooTest","group1").build();
		Trigger yahooTrigTest=TriggerBuilder.newTrigger().withIdentity("yahooTestTrgger","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * * ? * *")).build();
		yahooScheduler=new StdSchedulerFactory().getScheduler();
		yahooScheduler.start();
		yahooScheduler.scheduleJob(yahooDataTest, yahooTrigTest);
	}
}
