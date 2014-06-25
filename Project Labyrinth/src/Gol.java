import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Gol extends Monster {
	private final long nano=1000000000L;
	private BufferedImage projectile_img;
	private Game.Direction projectile_dir;
	private long time_since_transform;
	
	public Gol(int x, int y, int type) {
		super(x, y, type);
		try {getImg();} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void render(Graphics g) {
		checkState();
		if(isMovingAcrossScreen)
			super.updateLocation();
		if(!isOffScreen()){
			fireProjectile(g);
			g.drawImage(img,x,y,width,height,null);
		}
	}
	@Override
	public void transform() {
		previousState=img;
		oldtype=type;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}

	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7 && TransformedState==1){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10 && TransformedState==2){
			TransformedState=0;
			type=oldtype;
			img=previousState;
		}	
		if((System.nanoTime()-time_since_water)/nano>2000 && TransformedState==1 && isDrowning){
			TransformedState=3;
			img=Level.monsterState[2];
		}
		if((System.nanoTime()-time_since_water)/nano>3000 && TransformedState==3 && isDrowning){
			TransformedState=4;
			img=Level.monsterState[3];
		}
		if((System.nanoTime()-time_since_water)/nano>4000 && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
	}
	private void Kill_Respawn() {
		Gol me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	public Gol copy(){
		Gol clone=new Gol(oldX,oldY,oldtype);
		return clone;
	}

	private void fireProjectile(Graphics g) {
		if(canShoot){
				if(LineofSight()){
					projectile=new Projectile(x,y,projectile_img,projectile_dir);
					Sound.DragonSound.stop();
					Sound.DragonSound.setFramePosition(0);
					Sound.DragonSound.start();
					canShoot=false;
					projectile.projectile_speed=3;
				}
			}
		if(projectile!=null && !canShoot){
			projectile.render(g);
		}	
	}
	private void getImg() throws IOException {
		switch(type){
		case 7:	projectile_img=ImageIO.read(getClass().getResource("/tileset/projectile/dragon_shot_up.png"));
				super.img=Level.game_tileset[24];
				break;
		case 8:	projectile_img=ImageIO.read(getClass().getResource("/tileset/projectile/dragon_shot_down.png"));
				super.img=Level.game_tileset[25];
				break;
		case 9:	projectile_img=ImageIO.read(getClass().getResource("/tileset/projectile/dragon_shot_left.png"));
				super.img=Level.game_tileset[26];
				break;
		case 10:projectile_img=ImageIO.read(getClass().getResource("/tileset/projectile/dragon_shot_right.png"));
				super.img=Level.game_tileset[27];
				break;
		}
		
	}
	private boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Kill_Respawn();
			return true;
		}
		return false;
	}
	private boolean LineofSight() {
		switch(type){
		
		case 7: if((Character.x+Character.step==x || Character.x==x || Character.x-Character.step==x) && Character.y<y){
						projectile_dir=Game.Direction.Up;
						return true;
					}
					break;
		case 8:	if((x-Character.step==Character.x || x==Character.x|| x+Character.step==Character.x) && Character.y>y){
						projectile_dir=Game.Direction.Down;
						return true;
					}
					break;
		case 9:	if((Character.y-Character.step==y || Character.y+Character.step==y ||y==Character.y) && x>Character.x){
						projectile_dir=Game.Direction.Left;
						return true;
					}
					break;
		case 10:	if((Character.y-Character.step==y || Character.y+Character.step==y || y==Character.y) && x<Character.x){
						projectile_dir=Game.Direction.Right;
						return true;
					}
					break;
		}
		return false;
	}
	
}
