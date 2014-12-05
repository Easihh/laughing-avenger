import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Author
 * 
 * This is the Class that represent the Gol monster in game(Pink Dragon).This Monster special
 * ability is that it will awaken and start shooting the Hero if in Line of Sight once all the hearts
 * have been taken in a given Level.
 */
public class Gol extends Monster {
	private final long nano=1000000L;
	private BufferedImage projectile_img;
	private BufferedImage[] bullet_type;
	private Game.Direction projectile_dir;
	
	public Gol(int x, int y, ID type) {
		super(x, y, type);
		try {getProjectileImg();} catch (IOException e) {e.printStackTrace();}
		setupProjectileImage();	
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
		if((System.nanoTime()-time_since_water)/nano>duration_in_water && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
	}
	/**
	 *Main Method of the Gol Class that update its state and its animation as well as collision
	 *check*/
	public void update(){
		checkState();
		checkIfDrown();
		if(isMovingAcrossScreen)
			super.updateLocation();
		fireProjectile();
		if(projectile!=null && !canShoot)
			projectile.update();
		if(type==Tile.ID.boat && !Labyrinth.hero.isPushing){
			if(boat_movement)boatMovement();
			if(!boat_movement)
				boat_movement=true;
		}
	}

	@Override
	public void transform() {
		previousState=img;
		oldtype=type;
		type=Tile.ID.MoveableBlock;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}

	private void Kill_Respawn() {
		Gol me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	/*** Return a Copy of this object*/
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
	private void getProjectileImg() throws IOException {
		int row=2;
		int col=2;
		bullet_type = new BufferedImage[row*col];
		BufferedImage img=null;
		img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/Gol_Projectile.png"));
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				bullet_type[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }	
	}
	private void setupProjectileImage() {
		switch(type){
		case GolUp:		projectile_img=bullet_type[3];
						break;
		case GolDown:	projectile_img=bullet_type[0];
						break;
		case GolLeft:	projectile_img=bullet_type[1];
						break;
		case GolRight:	projectile_img=bullet_type[2];
						break;
		default:
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
	/* Gol Monsters should only be allowed to shoot if the hero is in detect range(partial or fully) within
	 * a single row X-axis(left,right) or column Y-axis(up,down) depending which direction the Monster is facing.
	 * Gol's Projectile should not be able to pass through solid object such as a wall(This is decided in the Character Class)                        .
	 */
	private boolean LineofSight() {
		Character hero=Labyrinth.hero;
		switch(type){
		case GolUp: 	if((hero.x+hero.step==x || hero.x==x || hero.x-hero.step==x) && hero.y<y){
							projectile_dir=Game.Direction.Up;
							return true;
						}
						break;
		case GolDown:	if((x-hero.step==hero.x || x==hero.x|| x+hero.step==hero.x) && hero.y>y){
							projectile_dir=Game.Direction.Down;
							return true;
						}
						break;
		case GolLeft:	if((hero.y-hero.step==y || hero.y+hero.step==y ||y==hero.y) && x>hero.x){
							projectile_dir=Game.Direction.Left;
							return true;
						}
						break;
		case GolRight:	if((hero.y-hero.step==y || hero.y+hero.step==y || y==hero.y) && x<hero.x){
							projectile_dir=Game.Direction.Right;
							return true;
						}
						break;
		default:
			break;
		}
		return false;
	}
	
}
