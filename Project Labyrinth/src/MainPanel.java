import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	static Level theLevel=new Level();
	private static final long serialVersionUID = 1L;
	private Font font;
	public static Character hero;
	private BufferedImage power_icon=null;
	public MainPanel(){
		setBackground(Color.black);	
		loadSound();
		font=new Font("Serif", Font.BOLD, 20);
		loadPower();
	}
	private void loadPower() {
		try {
			power_icon=ImageIO.read(getClass().getResource("/tileset/power_icon.png"));
			} catch (IOException e) {e.printStackTrace();}
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
		aSound=new Sound("PowerUsed");
		Sound.StageMusic.loop(Clip.LOOP_CONTINUOUSLY);	
	}
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			theLevel.render(g);
			g.setColor(Color.white);
			g.drawImage(Character.projectile_img[0],528,128,null);
			g.drawImage(power_icon, 528, 256,null);
			g.drawImage(power_icon, 528, 288,null);
			g.drawImage(power_icon, 528, 320,null);
			g.setFont(font);
			g.drawString("PW",528,240);
			g.drawString(""+Character.ammo,536,180);
			g.drawString("x:"+Character.x, 544, 64);
			g.drawString("y:"+Character.y, 544, 96);
			if(Labyrinth.GameState==Game.GameState.Death && !Character.Death.isDone)
				Character.Death.render(g);
			if(Labyrinth.GameState!=Game.GameState.Death)
				hero.render(g);
	}
}
