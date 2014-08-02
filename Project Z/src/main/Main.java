package main;
import java.awt.Dimension;
import javax.swing.JFrame;

import utility.Input;
import utility.Ressource;
import utility.Stopwatch;
import drawing.DrawPanel;

import java.awt.Color;

public class Main implements Runnable{
	private final int screenWidth=512,screenHeight=608;
	public static DrawPanel drawPane;
	public enum GameState{Normal,Paused,Menu};
	public static GameState gameStatus=GameState.Normal;
	private Thread thread;
	public static JFrame frame;
	private Stopwatch watch,fpsWatcher;
	private int fps_wanted=60,fps=0;
	private long refresh_delay=1000/fps_wanted;
	
	public static void main(String[] args) {
		new Main().start();
	}
	private void start() {
		watch=new Stopwatch();
		fpsWatcher=new Stopwatch();
		frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		new Ressource();
		drawPane = new DrawPanel();
		drawPane.setBackground(Color.BLACK);
		drawPane.setPreferredSize(new Dimension(screenWidth,screenHeight));
		frame.setContentPane(drawPane);
		frame.setJMenuBar(new Menu());
		frame.pack();
		frame.setVisible(true);
		drawPane.addKeyListener(new Input());
		thread=new Thread(this);
		thread.start();
	}
	@Override
	public void run() {
		watch.start();
		fpsWatcher.start();
		while(true){
		if((watch.elapsedMillis())>=refresh_delay){
			fps++;
				if(gameStatus==GameState.Normal){
					Hero.getInstance().update();
					Map.getInstance().update();
				}
				frame.repaint();
				watch.reset();
		}
		if((fpsWatcher.elapsedMillis())>=1000){
			frame.setTitle("Project:Z FPS:"+fps);
			fps=0;
			fpsWatcher.reset();
		}
	}
	}
}
