import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class DonMedusa extends Monster {
	private BufferedImage[] bullet_type;
	private Game.Direction projectile_dir;
	private final long nano=1000000L;
	private int index=0;
	private int nextFrame=500;
	long last_animation_update;
	public DonMedusa(int x, int y, int type) {
		super(x, y, type);
		canShoot=true;
		try {getImg();} catch (IOException e) {e.printStackTrace();}
		if(type==1)//left to right
			dir=Game.Direction.Left;
		else dir=Game.Direction.Down;
	}

	private void getImg() throws IOException {
		super.img=ImageIO.read(getClass().getResource("/tileset/DonMedusa.png"));
		setAnimation((BufferedImage)super.img, 2);
		bullet_type=new BufferedImage[4];
		bullet_type[0]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_right.png"));
		bullet_type[1]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_left.png"));
		bullet_type[2]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_down.png"));
		bullet_type[3]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_up.png"));
	}

	@Override
	public void transform() {}//impossible to hit this monster

	@Override
	public void render(Graphics g) {
		updateAnimation();
		move();
		fireProjectile(g);
		g.drawImage(super.animation.animation[index], x,y,width,height,null);
	}
	public void fireProjectile(Graphics g){
		if(canShoot){
			MultiDirectionSight();
		}
		if(projectile!=null && !canShoot)
			projectile.render(g);
	}
	public void updateAnimation(){
			if((System.nanoTime()-last_animation_update)/nano>nextFrame){
				last_animation_update=System.nanoTime();
				index++;
				if(index==super.maxFrame)
					index=0;
			}
	}
	private void MultiDirectionSight(){
		boolean shoot=false;
		/*Case Down*/
		if(Character.x==x  && y<Character.y){
				//hero found in line of sight
			// Check if there is an object inbetween
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[2];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if( x==Character.x && y>Character.y){
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((y==Character.y) && x>Character.x){
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if(y==Character.y && x<Character.x){
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
	}
}