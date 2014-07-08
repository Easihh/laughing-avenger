package com.easih.projectx;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class Main extends Activity {
	GameView myView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView=new GameView(this);
		setContentView(myView);
		setTitle("Project:X ");
	}
	@Override
	protected void onResume(){
		super.onResume();
		myView.resume();
		System.out.println("Resumed");
	}
	@Override
	protected void onPause(){
		super.onPause();
		myView.pause();
		System.out.println("PAUSEDe");
	}
	protected void onDestroy(){
		super.onDestroy();
		System.out.println("Destroyed");
		Input.mPlayer.release();
	}
	protected void onStop(){
		super.onStop();
		System.out.println("Stopped");
	}
	protected void onStart(){
		super.onStart();
		System.out.println("Started");
	}
	@Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
}
