package com.thanatos;
	
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private Scheduler yahooScheduler;
	static AnchorPane root;
	static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		try {
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
		JobDetail yahooData=JobBuilder.newJob(ImportDataFromYahooJob.class).withIdentity("yahoo","group1").build();
		Trigger yahooTrig=TriggerBuilder.newTrigger().withIdentity("yahooTrigger1","group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * 8-16 ? * MON-FRI")).build();
		yahooScheduler=new StdSchedulerFactory().getScheduler();
		yahooScheduler.start();
		yahooScheduler.scheduleJob(yahooData, yahooTrig);
	}
}
