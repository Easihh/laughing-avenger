import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;


public class Labyrinth extends JFrame {

	private static final long serialVersionUID = 1L;
	private MainPanel mainPane;
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Labyrinth() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(480,424));
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
			//final int max_movement_per_second=12;
			//int movement_tick=0;
			public void run(){
				while(true){
				fps++;
				if((System.nanoTime()-prev)<refresh_delay){
					long sleep=refresh_delay-(System.nanoTime()-prev);
					sleep/=1000000;
					try {
						Thread.sleep(sleep);
						//if(movement_tick!=max_movement_per_second){
							MainPanel.hero.update();
							//movement_tick++;
						//}
						repaint();
						prev=System.nanoTime();
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if((System.nanoTime()-timer)/nano>=1){						
					setTitle("Project:Labyrinth FPS:"+fps);
					//movement_tick=0;
					fps=0;
					timer=System.nanoTime();
				}
			}
		}
	}.start();
	}
}
