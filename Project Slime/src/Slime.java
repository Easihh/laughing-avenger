import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JTextField;



public class Slime extends JFrame {
	static boolean isInMenu=false;
	Screen myScreen=new Screen();
	static JLabel hitpoint=new JLabel();
	static JLayeredPane contentPane;
	private long nano=1000000000L;
	private long last_invincible;
	static Level themap;
	static int scroll_x;
	static int scroll_y;
	static int screensize_x;
	static int screensize_y;
	static Character hero;
	static Slime frame;
	static MenuOption menu;
	PlatformPanel platpanel;
	//private PlatformPanel platform=new PlatformPanel();
	
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
		hero=new Character(48, 144);
		last_invincible=System.nanoTime();
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
	public void checkMonsterCollision(){
		for(Monster aMonster:Level.monsterlist){
			aMonster.setMovement();
			for(Block ablock:Level.map){
				if(aMonster.mask.getBounds().intersects(ablock) && ablock.type==99){
					if(aMonster.dir==Monster.direction.Left)
						aMonster.dir=Monster.direction.Right;
					else aMonster.dir=Monster.direction.Left;
				}
			}
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
						checkMonsterCollision();
						checkCollision();
						hero.gravity();
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
