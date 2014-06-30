import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Gol extends Monster {
	
	private final long nano=1000000000L;
	private BufferedImage projectile_img;
	private Game.Direction projectile_dir;
	
	public Gol(int x, int y, int type) {
		super(x, y, type);
		try {getImg();} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void render(Graphics g) {
		if(!isOffScreen()){
			g.drawImage(img,x,y,width,height,null);
		}
		if(projectile!=null && !canShoot)
			projectile.render(g);
	}
	private void checkIfDrown() {
		if((System.nanoTime()-time_since_water)/nano>6000 && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
	}
	public void update(){
		checkState();
		checkIfDrown();
		if(isMovingAcrossScreen)
			super.updateLocation();
		fireProjectile();
		if(projectile!=null && !canShoot)
			projectile.update();
		if(type==Tile.ID.boat.value && !Character.isPushing){
			if(boat_movement)boatMovement();
			if(!boat_movement)
				boat_movement=true;
		}
	}

	@Override
	public void transform() {
		previousState=img;
		oldtype=type;
		type=Tile.ID.MoveableBlock.value;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;
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

	private void fireProjectile() {
		if(canShoot){
				if(LineofSight()){
					projectile=new Projectile(x,y,projectile_img,projectile_dir);
					Sound.DragonSound.setFramePosition(0);
					Sound.DragonSound.start();
					canShoot=false;
					projectile.projectile_speed=3;
				}
			}
		}	
	private void getImg() throws IOException {
		switch(type){
		case 24:	projectile_img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/dragon_shot_up.png"));
					break;
		case 25:	projectile_img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/dragon_shot_down.png"));
					break;
		case 26:	projectile_img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/dragon_shot_left.png"));
					break;
		case 27:	projectile_img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/dragon_shot_right.png"));
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
		Character hero=Character.getInstance();
		switch(type){
		case 24: if((hero.getX()+hero.step==x || hero.getX()==x || hero.getX()-hero.step==x) && hero.getY()<y){
						projectile_dir=Game.Direction.Up;
						return true;
					}
					break;
		case 25:	if((x-hero.step==hero.getX() || x==hero.getX()|| x+hero.step==hero.getX()) && hero.getY()>y){
						projectile_dir=Game.Direction.Down;
						return true;
					}
					break;
		case 26:	if((hero.getY()-hero.step==y || hero.getY()+hero.step==y ||y==hero.getY()) && x>hero.getX()){
						projectile_dir=Game.Direction.Left;
						return true;
					}
					break;
		case 27:	if((hero.getY()-hero.step==y || hero.getY()+hero.step==y || y==hero.getY()) && x<hero.getX()){
						projectile_dir=Game.Direction.Right;
						return true;
					}
					break;
		}
		return false;
	}
	
}
