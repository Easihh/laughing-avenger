import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
/* Author Enrico Talbot
 *  This is the basically the UI/Frame of the Game where everything is drawn and displayed.
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Animation flash_icon=null;
	private BufferedImage power_icon=null,hammer_icon=null;
	private final Font font=new Font("Serif", Font.BOLD, 18);
	public static Level theLevel;
	
	public MainPanel(){
		setBackground(Color.black);	
		loadSound();
		loadPower();
		theLevel=new Level();
	}
	/* Labyrinth Class "frame.repaint" call this function.
	 * This will draw all the tile in the Level,Effects,Monster and the Game UI 
	 * and its text and then the Hero.
	 */
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			theLevel.render(g);
			g.setColor(Color.white);
			flash_icon.setImage();
			g.drawImage(Game.projectile_img.get(0),528,160,null);
			drawFlashIcon(g);		
			drawPowerIcon(g);
			drawSpecialPower(g);
			g.setFont(font);
			drawText(g);
			if(Labyrinth.GameState==Game.GameState.Death && !Labyrinth.hero.Death.isDone)
				Labyrinth.hero.Death.render(g);
			if(Labyrinth.GameState!=Game.GameState.Death)
				Labyrinth.hero.render(g);
	}
	private void drawText(Graphics g) {
		g.drawString("LVL",528,54);
		g.drawString(""+Level.room,544,86);
		g.drawString("RM#",528,118);
		g.drawString(""+Level.remake,544,150);
		g.drawString("PW",528,240);
		g.drawString(""+Labyrinth.hero.ammo,544,212);
		g.drawString("x:"+Labyrinth.hero.x, 544, 400);
		g.drawString("y:"+Labyrinth.hero.y, 544, 432);
	}
	private void drawSpecialPower(Graphics g) {
		int tileSize=32;
		for(int i=0;i<3;i++){
			if(Level.Power[i]==Game.SpecialPower.Hammer)
				g.drawImage(hammer_icon, 529, 257+(tileSize*i),null);
			else if(Level.Power[i]==Game.SpecialPower.Ladder)
				g.drawImage(Game.game_tileset.get(Tile.ID.UpDownLadder.value), 529, 257+(tileSize*i),null);
			else if(Level.Power[i]==Game.SpecialPower.ArrowChange)
				g.drawImage(Game.game_tileset.get(Tile.ID.OneWayUp.value), 529, 257+(tileSize*i),null);
		}
	}
	private void drawPowerIcon(Graphics g) {
		if(!Labyrinth.hero.aPower.powerActivated_hammer && !Labyrinth.hero.aPower.powerActivated_ladder && !Labyrinth.hero.aPower.powerActivated_arrow){
			g.drawImage(power_icon, 528, 256,null);
			g.drawImage(power_icon, 528, 288,null);
			g.drawImage(power_icon, 528, 320,null);
		}
	}
	private void drawFlashIcon(Graphics g) {
		if(Labyrinth.hero.aPower.powerActivated_hammer || Labyrinth.hero.aPower.powerActivated_ladder || Labyrinth.hero.aPower.powerActivated_arrow){
			g.drawImage(flash_icon.getImage(), 528, 256,null);
			g.drawImage(flash_icon.getImage(), 528, 289,null);
			g.drawImage(flash_icon.getImage(), 528, 321,null);
		}
	}
	private void loadPower() {
		flash_icon=new Animation();
		BufferedImage[] img=new BufferedImage[4];
		BufferedImage anImg=null;
		try {
			power_icon=ImageIO.read(getClass().getResourceAsStream("/tileset/power_icon.png"));
			hammer_icon=ImageIO.read(getClass().getResourceAsStream("/tileset/hammer.png"));
			anImg=ImageIO.read(getClass().getResourceAsStream("/tileset/flashing_icon.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<4;j++){
					img[(i*2)+j]=anImg.getSubimage(j*33, i*33, 33, 33);
				 }
			 }
			flash_icon.AddScene(img[0], 150);
			flash_icon.AddScene(img[1], 150);
			flash_icon.AddScene(img[2], 150);
			flash_icon.AddScene(img[3], 150);
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
		aSound=new Sound("HammerPowerUsed");;
		aSound=new Sound("MonsterDestroyed");
		aSound=new Sound("ShotSound");
		aSound=new Sound("Death");
		aSound=new Sound("ArrowBridgePowerUsed");
		aSound=new Sound("Sleeper");
		aSound=new Sound("PowerEnabled");
		aSound=new Sound("Water");
		Sound.StageMusic.loop(Clip.LOOP_CONTINUOUSLY);	
	}
}
