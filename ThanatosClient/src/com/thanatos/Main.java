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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private Scheduler yahooScheduler;
	private AnchorPane root;
	public static Stage primaryStage;
	public static ApplicationContext ctx;
	@Override
	public void start(Stage primaryStage) {
		try {		
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
            FXMLLoader loader = new FXMLLoader();
            Main.primaryStage=primaryStage;
            loader.setLocation(getClass().getResource("TestOverview.fxml"));
            root = (AnchorPane) loader.load();
			Scene scene = new Scene(root,400,400);
			primaryStage.setTitle("Thanatos:Client");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			setupJobs();
			//primaryStage.setOnCloseRequest(e -> Platform.exit()){
				
		//	};
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
		try {
			yahooScheduler.shutdown();
		} 
		catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	private void setupJobs() throws SchedulerException {
		JobDetail yahooDataStart=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooStart","group1").build();
		JobDetail yahooDataEnd=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooEnd","group1").build();
		Trigger yahooTrig=TriggerBuilder.newTrigger().withIdentity("yahooTriggerStart","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 45-59/1 9-15 ? * MON-FRI")).build();
		Trigger yahooTrigEnd=TriggerBuilder.newTrigger().withIdentity("yahooTriggerEnd","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0-15/1 16 ? * MON-FRI")).build();
		yahooScheduler=new StdSchedulerFactory().getScheduler();
		yahooScheduler.start();
		yahooScheduler.scheduleJob(yahooDataStart, yahooTrig);
		yahooScheduler.scheduleJob(yahooDataEnd, yahooTrigEnd);
	}
}
