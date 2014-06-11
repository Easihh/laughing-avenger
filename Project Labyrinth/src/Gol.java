import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Gol extends Monster {
	private final long nano=1000000000L;
	private long time_since_transform;
	BufferedImage projectile_img;
	Game.Direction projectile_dir;
	public Gol(int x, int y, int type) {
		super(x, y, type);
		try {getImg();} catch (IOException e) {e.printStackTrace();}
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

	@Override
	public void transform() {
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
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

	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7 && TransformedState==1){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10 && TransformedState==2){
			TransformedState=0;
			type=1;
			img=previousState;
			//old_img=img;
		}	
	}
	public boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Gol aTile=this;
			aTile.x=oldX;
			aTile.y=oldY;
			aTile.type=oldtype;
			aTile.img=previousState;	
			aTile.isMovingAcrossScreen=false;
			aTile.TransformedState=0;
			Level.addRespawn(aTile);
			aTile.updateMask();
			Level.toRemove.add(aTile);//remove the tile if it goes offscreen
			return true;
		}
		return false;
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