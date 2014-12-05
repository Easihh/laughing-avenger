import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 * This class is the representation of the Medusa Object in Game.It has a special ability
 * where it freeze the Hero upon seeing it and shooting it as long as the hero is within its detect
 * range.
 */
public class Medusa extends Monster {
	private BufferedImage[] bullet_type;
	private BufferedImage projectile_img;
	private Game.Direction projectile_dir;

	public Medusa(int x, int y, ID type) {
		super(x, y, type);
		try {getProjectileImage();} catch (IOException e) {e.printStackTrace();}
		canShoot=true;
	}
	@Override
	public void render(Graphics g) {
		if(projectile!=null && !canShoot)
			projectile.render(g);
		g.drawImage(img,x,y,width,height,null);
	}
	@Override
	public void transform() {}//impossible to shot this monster
	/**
	 *Main Method of the Medusa Class that update its state  as well as collision
	 *check*/
	public void update(){
		if(canShoot)
			MultiDirectionSight();
		if(projectile!=null && !canShoot)
			projectile.update();
	}
	private void getProjectileImage() throws IOException{
		int row=2;
		int col=2;
		bullet_type=new BufferedImage[row*col];
		BufferedImage img=null;
		img=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/Medusa_Projectile.png"));
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				bullet_type[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
	}
	/* Medusa Monster should only be allowed to shoot if the Hero is fully in detect range within
	 * a single row X-axis(left,right) or column Y-axis(up,down).Medusa should not be able to shoot 
	 * the Hero even if he is in full detect range if there are solid object such as a wall in-between
	 * the Monster and the Hero.If the hero is partially in range, the Medusa should change to show 
	 * that the player is close to being in fully detect range and automatically die.
	 */
	private void MultiDirectionSight(){
		boolean shoot=false;
		boolean inRange=false;
		Character hero=Labyrinth.hero;
		/*Case Down*/
		if((Math.abs(hero.x-x) <=hero.step) && y<hero.y){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if(hero.x==x  && y<hero.y){
				//hero found in line of sight
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[0];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if((Math.abs(hero.x-x)<=hero.step|| hero.x-hero.step==x) && y>hero.y){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if( x==hero.x && y>hero.y){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((Math.abs(hero.y-y) <=hero.step)&& x>hero.x){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if((y==hero.y) && x>hero.x){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if((Math.abs(hero.y-y) <=hero.step) && x<hero.x){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if(y==hero.y && x<hero.x){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
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
		if(!inRange)
			img=Game.game_tileset.get(Tile.ID.SleepMedusa.value);
	}
}
