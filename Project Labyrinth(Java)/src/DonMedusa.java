import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 * This Monster is represented as a pink monster that move in a given direction.It's special
 * ability is to freeze and shoot player in its detect range(same as Medusa class) except this Monster
 * can move.
 */
public class DonMedusa extends Monster {
	private Animation DonMedusaAnimation;
	private BufferedImage[] bullet_type,spriteSheet;
	private BufferedImage projectile_img;
	private Game.Direction projectile_dir;
	private final int frameDuration=500,step=2;
	
	public DonMedusa(int x, int y, ID type) {
		super(x, y, type);
		DonMedusaAnimation=new Animation();
		canShoot=true;
		try {getImg();} catch (IOException e) {e.printStackTrace();}
		//decides if its going to be moveing from left->right->left  or Down->Up->Down
		if(type==Tile.ID.LeftRightDonMedusa)
			dir=Game.Direction.Left;
		else dir=Game.Direction.Down;
	}

	private void getImg() throws IOException {
		int row=1;
		int col=2;
		BufferedImage img=null;
		spriteSheet=new BufferedImage[row*col];
		img=ImageIO.read(getClass().getResource("/tileset/DonMedusa.png"));
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				spriteSheet[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		DonMedusaAnimation.AddScene(spriteSheet[0], frameDuration);
		DonMedusaAnimation.AddScene(spriteSheet[1], frameDuration);
		getProjectile();
	}

	private void getProjectile() throws IOException {
		int row=2;
		int col=2;
		bullet_type=new BufferedImage[row*col];
		BufferedImage img=null;
		img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/DonMedusa_Projectile.png"));
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				bullet_type[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
	}

	@Override
	public void render(Graphics g) {
		if(projectile!=null && !canShoot)
			projectile.render(g);
		g.drawImage(DonMedusaAnimation.getImage(), x,y,width,height,null);
	}
	@Override
	public void transform() {}//impossible to hit this monster
	/**
	 *Main Method of the Don-Medusa Class that update its state and its animation as well as collision
	 *check*/
	public void update(){
		fireProjectile();
		if(Labyrinth.GameState==Game.GameState.Normal)
			move();
		fireProjectile();
		DonMedusaAnimation.setImage();
		if(projectile!=null && !canShoot)
			projectile.update();
		updateMask();
	}
	
	private void fireProjectile(){
		if(canShoot)
			MultiDirectionSight();
	}
	/* This monster move to one direction until it collide with something and then reverse direction 
	 * to a different one.
	 */
	private void move() {
		switch(dir){
		case Left: 	if(checkCollision(new Rectangle(x-step, y,step,half_height),new Rectangle(x-step, y+half_height,step,half_height)))
						dir=Game.Direction.Right;
					else x-=step;
					break;
		case Right: if(checkCollision(new Rectangle(x+width, y,step,half_height),new Rectangle(x+width, y+half_height,step,half_height)))
						dir=Game.Direction.Left;
					else x+=step;
					break;
		case Up: 	if(checkCollision(new Rectangle(x, y-step,half_width,step),new Rectangle(x+half_width, y-step,half_width,step)))
						dir=Game.Direction.Down;
					else y-=step;
					break;
		case Down: if(checkCollision(new Rectangle(x, y+height,half_width,step),new Rectangle(x+half_width, y+height,half_width,step)))
						dir=Game.Direction.Up;
					else y+=step;
					break;
		default:
			break;
		}
	}
	/* Don Medusa Monster should only be allowed to shoot if the Hero is fully in detect range within
	 * a single row X-axis(left,right) or column Y-axis(up,down).Medusa should not be able to shoot 
	 * the Hero even if he is in full detect range if there are solid object such as a wall in-between
	 * the Monster and the Hero.
	 */
	private void MultiDirectionSight(){
		boolean shoot=false;
		Character hero=Labyrinth.hero;
		/*Case Down*/
		if(hero.x==x  && y<hero.y){
				//hero found in line of sight
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[0];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if( x==hero.x && y>hero.y){
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((y==hero.y) && x>hero.x){
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if(y==hero.y && x<hero.x){
			if(!Object_inBetween("Right")){
				shoot=true;
				projectile_img=bullet_type[2];
				projectile_dir=Game.Direction.Right;
			}
		}
		if(shoot){
			projectile=new Projectile(x,y,projectile_img,projectile_dir);
			Sound.MedusaSound.start();
			canShoot=false;
			projectile.projectile_speed=6;
			Sound.StageMusic.stop();
			Labyrinth.GameState=Game.GameState.Paused;
		}
	}
}
