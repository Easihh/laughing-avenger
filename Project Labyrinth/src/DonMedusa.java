import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class DonMedusa extends Monster {
	private Animation DonMedusaAnimation;
	private BufferedImage[] bullet_type;
	private BufferedImage projectile_img;
	private BufferedImage[] spriteSheet;
	private Game.Direction projectile_dir;
	
	public DonMedusa(int x, int y, int type) {
		super(x, y, type);
		DonMedusaAnimation=new Animation();
		canShoot=true;
		try {getImg();} catch (IOException e) {e.printStackTrace();}
		if(type==1)//left to right
			dir=Game.Direction.Left;
		else dir=Game.Direction.Down;
	}

	private void getImg() throws IOException {
		BufferedImage img=null;
		spriteSheet=new BufferedImage[2];
		img=ImageIO.read(getClass().getResource("/tileset/DonMedusa.png"));
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<2;j++){
				spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		DonMedusaAnimation.AddScene(spriteSheet[0], 500);
		DonMedusaAnimation.AddScene(spriteSheet[1], 500);
		bullet_type=new BufferedImage[4];
		bullet_type[0]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_right.png"));
		bullet_type[1]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_left.png"));
		bullet_type[2]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_down.png"));
		bullet_type[3]=ImageIO.read(getClass().getResource("/tileset/projectile/DonMedusa_shot_up.png"));
	}

	@Override
	public void render(Graphics g) {
		if(projectile!=null && !canShoot)
			projectile.render(g);
		g.drawImage(DonMedusaAnimation.getImage(), x,y,width,height,null);
	}
	@Override
	public void transform() {}//impossible to hit this monster
	
	public void update(){
		fireProjectile();
		if(Labyrinth.GameState==Game.GameState.Normal)
			move();
		fireProjectile();
		DonMedusaAnimation.setImage();
	}
	
	private void fireProjectile(){
		if(canShoot)
			MultiDirectionSight();
	}
	private void move() {
		switch(dir){
		case Left: 	if(checkCollison(x-1, y, x-1, y+height-1)){
						dir=Game.Direction.Right;
					}else x-=2;
					break;
		case Right: if(checkCollison(x+width, y+height-1, x+width, y)){
						dir=Game.Direction.Left;
					}else x+=2;
					break;
		case Up: if(checkCollison(x+width-1, y-1, x, y-1)){
						dir=Game.Direction.Down;
					}else
					y-=2;
					break;
		case Down: if(checkCollison(x+width-1, y+height, x, y+height)){
					dir=Game.Direction.Up;
					}else
					y+=2;
					break;
		}
		updateMask();
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
			Sound.StageMusic.stop();
			Labyrinth.GameState=Game.GameState.Paused;
		}
	}
}
