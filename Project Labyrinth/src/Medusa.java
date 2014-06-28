import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Medusa extends Monster {
	private BufferedImage[] bullet_type;
	private BufferedImage projectile_img;
	private Game.Direction projectile_dir;

	public Medusa(int x, int y, int type) {
		super(x, y, type);
		try {getImage();} catch (IOException e) {e.printStackTrace();}
		canShoot=true;
	}
	@Override
	public void render(Graphics g) {
		fireProjectile(g);
		g.drawImage(img,x,y,width,height,null);
	}
	@Override
	public void transform() {}//impossible to shot this monster
	private void fireProjectile(Graphics g){
		if(canShoot){
			MultiDirectionSight();
		}
		if(projectile!=null && !canShoot)
			projectile.render(g);
	}
	private void getImage() throws IOException{
		img=Game.game_tileset.get(7);
		bullet_type=new BufferedImage[4];
		bullet_type[0]=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/medusa_shot_right.png"));
		bullet_type[1]=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/medusa_shot_left.png"));
		bullet_type[2]=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/medusa_shot_down.png"));
		bullet_type[3]=ImageIO.read(getClass().getResourceAsStream("/tileset/projectile/medusa_shot_up.png"));
	}
	private void MultiDirectionSight(){
		boolean shoot=false;
		boolean inRange=false;
		Character hero=Character.getInstance();
		/*Case Down*/
		if((Math.abs(hero.getX()-x) <=hero.step) && y<hero.getY()){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if(hero.getX()==x  && y<hero.getY()){
				//hero found in line of sight
			// Check if there is an object inbetween
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);//change to awaken medusa;
			inRange=true;
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[2];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if((Math.abs(hero.getX()-x)<=hero.step|| hero.getX()-hero.step==x) && y>hero.getY()){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if( x==hero.getX() && y>hero.getY()){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);//change to awaken medusa;
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((Math.abs(hero.getY()-y) <=hero.step)&& x>hero.getX()){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if((y==hero.getY()) && x>hero.getX()){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);//change to awaken medusa;
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if((Math.abs(hero.getY()-y) <=hero.step) && x<hero.getX()){
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);
			inRange=true;
		}
		if(y==hero.getY() && x<hero.getX()){
			inRange=true;
			img=Game.game_tileset.get(Tile.ID.MedusaAwaken.value);//change to awaken medusa;
			if(!Object_inBetween("Right")){
				shoot=true;
				projectile_img=bullet_type[0];
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
