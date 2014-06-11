import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Medusa extends Monster {
	private BufferedImage[] bullet_type;
	private Game.Direction projectile_dir;
	public Medusa(int x, int y, int type) {
		super(x, y, type);
		try {getImage();} catch (IOException e) {e.printStackTrace();}
		canShoot=true;
	}
	public void getImage() throws IOException{
		img=Level.game_tileset[7];
		bullet_type=new BufferedImage[4];
		bullet_type[0]=ImageIO.read(getClass().getResource("/tileset/projectile/medusa_shot_right.png"));
		bullet_type[1]=ImageIO.read(getClass().getResource("/tileset/projectile/medusa_shot_left.png"));
		bullet_type[2]=ImageIO.read(getClass().getResource("/tileset/projectile/medusa_shot_down.png"));
		bullet_type[3]=ImageIO.read(getClass().getResource("/tileset/projectile/medusa_shot_up.png"));
	}
	@Override
	public void transform() {}//impossible to shot this monster

	@Override
	public void render(Graphics g) {
		fireProjectile(g);
		g.drawImage(img,x,y,width,height,null);
	}
	public void fireProjectile(Graphics g){
		if(canShoot){
			MultiDirectionSight();
		}
		if(projectile!=null && !canShoot)
			projectile.render(g);
	}
	private void MultiDirectionSight(){
		boolean shoot=false;
		boolean inRange=false;
		/*Case Down*/
		if((Character.x+Character.step==x|| Character.x-Character.step==x) && y<Character.y){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if(Character.x==x  && y<Character.y){
				//hero found in line of sight
			// Check if there is an object inbetween
			img=Level.game_tileset[14];//change to awaken medusa;
			inRange=true;
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[2];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if((Character.x+Character.step==x|| Character.x-Character.step==x) && y>Character.y){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if( x==Character.x && y>Character.y){
			inRange=true;
			img=Level.game_tileset[14];//change to awaken medusa;
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((Character.y+Character.step==y|| Character.y-Character.step==y) && x>Character.x){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if((y==Character.y) && x>Character.x){
			inRange=true;
			img=Level.game_tileset[14];//change to awaken medusa;
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if((Character.y+Character.step==y|| Character.y-Character.step==y) && x<Character.x){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if(y==Character.y && x<Character.x){
			inRange=true;
			img=Level.game_tileset[14];//change to awaken medusa;
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
		}
		if(!inRange)
			img=Level.game_tileset[7];
	}
}
