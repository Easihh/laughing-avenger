package main;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import utility.Input;
import utility.Ressource;
import utility.Stopwatch;
import drawing.DrawPanel;

import java.awt.Color;

public class Main extends JFrame{
	private static final long serialVersionUID = 4648172894076113183L;
	private final int screenWidth=512;
	private final int screenHeight=608;
	public static DrawPanel drawPane;
	public static Main frame;
	public enum GameState{Normal,Paused,Menu};
	public static GameState gameStatus=GameState.Normal;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Main(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		new Ressource();
		drawPane = new DrawPanel();
		drawPane.setBackground(Color.BLACK);
		drawPane.setPreferredSize(new Dimension(screenWidth,screenHeight));
		setContentPane(drawPane);
		setJMenuBar(new Menu());
		pack();
		drawPane.addKeyListener(new Input());
		start();
	}
	private void start() {
		new Thread(){
			Stopwatch watch=new Stopwatch();
			Stopwatch fpsWatcher=new Stopwatch();
			int fps_wanted=60;
			long refresh_delay=1000/fps_wanted;
			int fps=0;
			public void run(){
				watch.start();
				fpsWatcher.start();
				while(true){
				if((watch.elapsedMillis())>=refresh_delay){
					fps++;
						if(gameStatus==GameState.Normal){
							Hero.getInstance().update();
							Map.getInstance().update();
						}
						repaint();
						watch.reset();
				}
				if((fpsWatcher.elapsedMillis())>=1000){
					setTitle("Project:Z FPS:"+fps);
					fps=0;
					fpsWatcher.reset();
				}
			}
		}
	}.start();
	}
}
