package main;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

import utility.Sound;
import utility.Stopwatch;

public class Attack {
	public MovingSword theSword;
	public SwordEffect sEffect;
	private static Stopwatch sEffectTimer;
	public Sword mySword;
	Stopwatch last_sword,last_moving_sword;
	public static Vector<BufferedImage> attackPose;
	public Attack(){
		last_sword=new Stopwatch();
		last_moving_sword=new Stopwatch();
		last_sword.start();
		last_moving_sword.start();
		try {loadAttackPose();
		} catch (IOException e) {e.printStackTrace();}
	}
	private void loadAttackPose() throws IOException {
		int tileSize=32;
		attackPose=new Vector<BufferedImage>();
		BufferedImage img=ImageIO.read(getClass().getResourceAsStream("/Map/Link_Attack.png"));
		int row=img.getHeight()/tileSize;
		int col=img.getWidth()/tileSize;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				attackPose.add(img.getSubimage(j*tileSize, i*tileSize, tileSize, tileSize));
			}
		}
	}
	public void tryAttack(Hero hero) {
		if(mySword==null && hero.attack_img==null && last_sword.elapsedMillis()>500){
			Sound.sword.setFramePosition(0);
			Sound.sword.start();
			last_sword.reset();
			mySword=new Sword(hero);
			hero.isAttacking=true;
			getAttackPose(hero);
			if(theSword==null && last_moving_sword.elapsedMillis()>1000 && hero.currentHealth==hero.maxHealth){
				Sound.swordCombine.setFramePosition(0);
				Sound.swordCombine.start();
				last_moving_sword.reset();
				theSword=new MovingSword(hero);
				hero.canAttack=false;
			}
		}
	}
	private void getAttackPose(Hero hero) {
		switch(hero.dir){
		case Left:	hero.attack_img=attackPose.get(2);
					break;
		case Right:	hero.attack_img=attackPose.get(3);
					break;
		case Up:	hero.attack_img=attackPose.get(0);
					break;
		case Down:	hero.attack_img=attackPose.get(1);
					break;
		}
		
	}
	public void createSwordEffect(Point collision_point){
		sEffect=new SwordEffect(collision_point);
		sEffectTimer=new Stopwatch();
		sEffectTimer.start();
	}
	public void render(Graphics g){
		if(theSword!=null)
			theSword.render(g);
		if(mySword!=null)
			mySword.render(g);
		if(sEffect!=null)
			sEffect.render(g);
	}
	public void update(){
		if(theSword!=null)
			theSword.update();
		if(mySword!=null)
			mySword.update(this);
		if(sEffect!=null && sEffectTimer.elapsedMillis()<200)
			sEffect.update();
		if(sEffect!=null && sEffectTimer.elapsedMillis()>200)
			sEffect=null;
	}
}
