import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;


public class Labyrinth extends JFrame {

	private static final long serialVersionUID = 1L;
	private MainPanel mainPane;
	enum GameState{Normal,Paused,Menu}
	static GameState GameState;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Labyrinth frame = new Labyrinth();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Labyrinth() {
		GameState=GameState.Paused;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(672,560));
		setResizable(false);
		mainPane = new MainPanel();
		setContentPane(mainPane);
		addKeyListener(new Input());
		start();
	}
	public void start(){
		new Thread(){
			long nano=1000000000L;
			int fps_wanted=60;
			long refresh_delay=nano/fps_wanted;
			long timer=0;
			long prev=System.nanoTime();
			int fps=0;
			public void run(){
				while(true){
				fps++;
				if((System.nanoTime()-prev)<refresh_delay){
					long sleep=refresh_delay-(System.nanoTime()-prev);
					sleep/=1000000;
					try {
						Thread.sleep(sleep);
						//if(GameState!=GameState.Paused)
							MainPanel.hero.update();
						repaint();
						prev=System.nanoTime();
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if((System.nanoTime()-timer)/nano>=1){						
					setTitle("Project:Labyrinth FPS:"+fps);
					fps=0;
					timer=System.nanoTime();
				}
			}
		}
	}.start();
	}
}
