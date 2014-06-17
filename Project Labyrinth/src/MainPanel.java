import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.JLabel;


public class MainPanel extends JPanel {
	static Level theLevel=new Level();
	private static final long serialVersionUID = 1L;
	private Font font;
	JLabel xposition,yposition;
	public static Character hero;
	public MainPanel(){
		setBackground(Color.black);	
		loadSound();
		font=new Font("Serif", Font.BOLD, 24);
	}
	private void loadSound() {
		@SuppressWarnings("unused")
		Sound aSound; 
		aSound=new Sound("StageMusic");
		aSound=new Sound("HeartSound");
		aSound=new Sound("DoorOpen");
		aSound=new Sound("MedusaSound");
		aSound=new Sound("DragonSound");
		aSound=new Sound("ChestOpen");
		aSound=new Sound("MonsterDestroyed");
		aSound=new Sound("ShotSound");
		aSound=new Sound("Death");
		Sound.StageMusic.loop(Clip.LOOP_CONTINUOUSLY);	
	}
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			theLevel.render(g);
			g.setColor(Color.white);
			g.drawImage(Character.projectile_img[0],528,128,null);
			g.setFont(font);
			g.drawString(""+Character.ammo,536,180);
			g.drawString("x:"+Character.x, 544, 64);
			g.drawString("y:"+Character.y, 544, 96);
			if(Labyrinth.GameState==Game.GameState.Death && !Character.Death.isDone)
				Character.Death.render(g);
			if(Labyrinth.GameState!=Game.GameState.Death)
				hero.render(g);
	}
}
