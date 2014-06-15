import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Labyrinth extends JFrame{

	private static final long serialVersionUID = 1L;
	private MainPanel mainPane;
	static long prev;
	static Game.GameState GameState;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
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
		GameState=GameState.Normal;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(672,540));
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
				if(System.nanoTime()-prev>=refresh_delay){
					prev=System.nanoTime();
				}
			}
		}
	}.start();
	}
}
