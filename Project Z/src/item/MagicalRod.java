package item;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import utility.Sound;
import utility.Stopwatch;

import main.Attack;
import main.Hero;
import main.Projectile;


public class MagicalRod extends Item {
	public Projectile projectile;
	private Vector<BufferedImage> projectile_imgs=new Vector<BufferedImage>();
	private Vector<BufferedImage> rods_imgs=new Vector<BufferedImage>();
	private BufferedImage theRod=null;
	private int theRodX,theRodY;
	private final long attack_delay=350;
	private Stopwatch last_attack;
	private boolean showRod=false;
	public MagicalRod(int x, int y, ID type) {
		super(x, y, type);
		inventoryX=x;
		InventoryY=y;
		this.type=type;
		last_attack=new Stopwatch();
		last_attack.start();
		loadImages();
	}
	private void loadImages() {
		BufferedImage image=null;
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/MagicalRod.png"));
		
		image=ImageIO.read(getClass().getResourceAsStream("/map/MagicalRod_Projectiles.png"));
		for(int i=0;i<image.getHeight()/32;i++){
			for(int j=0;j<image.getWidth()/32;j++)
				projectile_imgs.add(image.getSubimage(j*32, i*32, 32, 32));
		}
		image=ImageIO.read(getClass().getResourceAsStream("/map/MagicalRods.png"));
		for(int i=0;i<image.getHeight()/32;i++){
			for(int j=0;j<image.getWidth()/32;j++)
				rods_imgs.add(image.getSubimage(j*32, i*32, 32, 32));
		}
	}
		catch (IOException e) {e.printStackTrace();}	
	}
	@Override
	public void render(Graphics g) {
		if(projectile!=null){
			projectile.render(g);
		}
		if(showRod)
			g.drawImage(theRod,theRodX,theRodY,null);
	}

	@Override
	public void use() {
		Hero hero=Hero.getInstance();
		if(last_attack!=null && last_attack.elapsedMillis()>attack_delay){
			Sound.magicalRod.setFramePosition(0);
			Sound.magicalRod.start();
			hero.attack_img=getAttackImage(hero);
			hero.isAttacking=true;
			last_attack.reset();
			projectile=new Projectile(getInfoRect(hero), getProjectileImage(hero), hero.dir);
			setRodImage(hero);
			showRod=true;
		}
	}

	private void setRodImage(Hero hero) {
		switch(hero.dir){
		case Left: 	theRodX=hero.x-24;
					theRodY=hero.y;
					theRod=rods_imgs.get(2);
					break;
		case Right: theRodX=hero.x+24;
					theRodY=hero.y;
					theRod=rods_imgs.get(3);
					break;
		case Up: 	theRodX=hero.x;
					theRodY=hero.y-24;
					theRod=rods_imgs.get(0);
					break;
		case Down:	theRodX=hero.x;
					theRodY=hero.y+24;
					theRod=rods_imgs.get(1);
					break;
		}
	}
	private Image getAttackImage(Hero hero) {
		switch(hero.dir){
		case Left: 	return Attack.attackPose.get(2);
		case Right: return Attack.attackPose.get(3);
		case Up: 	return Attack.attackPose.get(0);
		case Down:	return Attack.attackPose.get(1);
		}
		return null;
	}
	private Rectangle getInfoRect(Hero hero) {
		switch(hero.dir){
		case Left: 	return new Rectangle(hero.x-32,hero.y,32,32);
		case Right: return new Rectangle(hero.x+32,hero.y,32,32);
		case Up: 	return new Rectangle(hero.x,hero.y-32,32,32);
		case Down:	return new Rectangle(hero.x,hero.y+32,32,32);
		}
		return null;
	}
	private BufferedImage getProjectileImage(Hero hero) {
		switch(hero.dir){
		case Left: 	return projectile_imgs.get(2);
		case Right: return projectile_imgs.get(3);
		case Up: return projectile_imgs.get(0);
		case Down: return projectile_imgs.get(1);
		}
		return null;
	}
	@Override
	public void update() {
		if(projectile!=null){
			projectile.update();
		}
		if(last_attack.elapsedMillis()>attack_delay){
			Hero.getInstance().isAttacking=false;
			showRod=false;
		}
		if(projectile!=null && projectile.outOfBound()){
			projectile=null;
			Hero.getInstance().isAttacking=false;
		}
	}

}
