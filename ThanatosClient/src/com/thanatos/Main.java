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
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private Scheduler yahooScheduler;
	private AnchorPane root;
	public static Stage primaryStage;
	public static ApplicationContext ctx;
	public static Double screenWidth;
	public static Double screenHeight;
	@Override
	public void start(Stage primaryStage) {
		try {
			ctx=new ClassPathXmlApplicationContext("Spring.xml");
            FXMLLoader loader = new FXMLLoader();
            Main.primaryStage=primaryStage;
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			screenWidth=primaryScreenBounds.getWidth();
			screenHeight=primaryScreenBounds.getHeight();
            loader.setLocation(getClass().getResource("TestOverview.fxml"));
            root = (AnchorPane) loader.load();
			Scene scene = new Scene(root,600,800);
			primaryStage.setTitle("Thanatos:Client");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);
			setupJobs();
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
		JobDetail yahooDataDay=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooDay","group1").build();
		JobDetail yahooDataEndDay=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooEndDay","group1").build();
		//JobDetail yahooDataTest=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahooTest","group1").build();
		//Trigger yahooTrigTest=TriggerBuilder.newTrigger().withIdentity("yahooTestTrgger","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * * ? * *")).build();
		Trigger yahooTrigStartDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerStart","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 45-59/1 9 ? * MON-FRI")).build();
		Trigger yahooTrigDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerDay","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * 10-15 ? * MON-FRI")).build();
		Trigger yahooTrigEndDay=TriggerBuilder.newTrigger().withIdentity("yahooTriggerEndDay","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0-15/1 16 ? * MON-FRI")).build();
		yahooScheduler=new StdSchedulerFactory().getScheduler();
		yahooScheduler.start();
		//yahooScheduler.scheduleJob(yahooDataTest, yahooTrigTest);
		yahooScheduler.scheduleJob(yahooDataStart, yahooTrigStartDay);
		yahooScheduler.scheduleJob(yahooDataDay, yahooTrigDay);
		yahooScheduler.scheduleJob(yahooDataEndDay, yahooTrigEndDay);
	}
}
