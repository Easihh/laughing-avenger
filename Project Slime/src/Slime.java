import java.awt.DisplayMode;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;




public class Slime extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static boolean isInMenu=false;
	Screen myScreen=new Screen();
	static JLabel hitpoint=new JLabel();
	static JLayeredPane contentPane;
	private long nano=1000000000L;
	private long last_invincible;
	private long last_monsterspawn;
	static Level themap;
	static int scroll_x;
	static int scroll_y;
	static int screensize_x;
	static int screensize_y;
	static Character hero;
	static Slime frame;
	static MenuOption menu;
	PlatformPanel platpanel;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Slime();
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
	public Slime() {
		themap=new Level();
		screensize_x=500;
		screensize_y=500;
		scroll_x=screensize_x/2;
		scroll_y=screensize_y/2;
		hero=new Character(96, 144);
		last_invincible=System.nanoTime();
		last_monsterspawn=System.nanoTime();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setIgnoreRepaint(true);
		DisplayMode dm=new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		setBounds(0, 0, screensize_x, screensize_y);
		setLocationRelativeTo(null);
		contentPane = new JLayeredPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		platpanel=new PlatformPanel();
		platpanel.setBounds(0, 0, screensize_x, screensize_y);
		contentPane.add(platpanel);	
		addKeyListener(new Input());
		start();
	}
	public void checkMonster(){
		for(Monster aMonster:Level.monsterlist){
			aMonster.update();
		}
	}
	public void monsterSpawn(){
		if((System.nanoTime()-last_monsterspawn)/nano >5){
			Level.monsterlist.add(new Monster(144,384,1));
			last_monsterspawn=System.nanoTime();
		}
	}
	public void checkCollision(){
		if((System.nanoTime()-last_invincible)/nano>=0.5)
			hero.isInvincible=false;
		for(Monster aMonster:Level.monsterlist){
			if(aMonster.mask.intersects(hero.mastermask) && !hero.isInvincible){
				hero.current_health-=5;
				hero.isInvincible=true;
				last_invincible=System.nanoTime();
			}
		}
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
						checkMonster();
						checkCollision();
						hero.gravity();
						monsterSpawn();
						repaint();
						prev=System.nanoTime();
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if((System.nanoTime()-timer)/nano>=1){	
					//platform.repaint();					
					setTitle("Project:Slime FPS:"+fps);
					fps=0;
					timer=System.nanoTime();
				}
			}
		}
	}.start();
		//new GameUpdate().start();
	}
}
