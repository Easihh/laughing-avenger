import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Toolkit;
/**
 * This is the Main Class of the Game.This class is a Thread different than the JFrame UI thread 
 * because repainting with a JFrame should be done in a different Thread to prevent with redrawing constantly.
 */

public class Labyrinth implements Runnable{
	private MainPanel mainPane;
	/*** Whether the Level has bene loaded yet*/
	public static boolean level_is_loaded=false;
	/*** Current Game State*/
	public static Game.GameState GameState;
	/*** Character Object */
	public static Character hero;
	private final int ScreenWidth=672,ScreenHeight=512,fps_wanted=60;
	private Thread t=null;
	private JFrame frame;
	private StopWatch watch,fpswatch;
	private int fps=0;
	private long refresh_delay=1000/fps_wanted;
	private Input input;
	public static void main(String[] args) {
		new Labyrinth().setup();
	}

	private void  setup() {
		watch=new StopWatch();
		fpswatch=new StopWatch();
		frame=new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Labyrinth.class.getResource("/tileset/Game_Icon.png")));
		GameState=Game.GameState.NotStarted;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Game();
		mainPane = new MainPanel();
		mainPane.setPreferredSize(new Dimension(ScreenWidth,ScreenHeight));
		frame.setContentPane(mainPane);
		frame.pack();
		input=new Input();
		frame.addKeyListener(input);
		frame.setVisible(true);
		t=new Thread(this);
		t.start();
	}
	/** The Game Update and refresh screen Loop*/
	public void run(){
		watch.start();
		fpswatch.start();
		while(true){
			if(watch.elapsedMillis()>=refresh_delay){
				fps++;
				if(Labyrinth.level_is_loaded){
					Labyrinth.hero.update(input);	
				}
				if(GameState!=Game.GameState.NotStarted && Labyrinth.level_is_loaded){
					for(int i=0;i<Level.map_tile.size();i++)
						Level.map_tile.get(i).update();
					}
				watch.reset();
				frame.repaint();//call paintComponents of the MainPanel class to redraw everything
			}
			if(fpswatch.elapsedMillis()>=1000){						
				frame.setTitle("Project:Labyrinth FPS:"+fps);
				fps=0;
				fpswatch.reset();
				}
			}
		}
}
