import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DonMedusa extends Monster {
	private Animation DonMedusaAnimation;
	private BufferedImage[] bullet_type;
	private BufferedImage projectile_img;
	private BufferedImage[] spriteSheet;
	private Game.Direction projectile_dir;
	private final int frameDuration=500;
	private final int step=2;
	
	public DonMedusa(int x, int y, ID type) {
		super(x, y, type);
		DonMedusaAnimation=new Animation();
		canShoot=true;
		try {getImg();} catch (IOException e) {e.printStackTrace();}
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
		}
	}
	private void MultiDirectionSight(){
		boolean shoot=false;
		Character hero=Character.getInstance();
		/*Case Down*/
		if(hero.getX()==x  && y<hero.getY()){
				//hero found in line of sight
			if(!Object_inBetween("Down")){
				shoot=true;
				projectile_img=bullet_type[0];
				projectile_dir=Game.Direction.Down;
			}
		}
		/*Case Up*/
		if( x==hero.getX() && y>hero.getY()){
			if(!Object_inBetween("Up")){
				shoot=true;
				projectile_img=bullet_type[3];
				projectile_dir=Game.Direction.Up;
			}
		}
		/*Case Left*/
		if((y==hero.getY()) && x>hero.getX()){
			if(!Object_inBetween("Left")){
				shoot=true;
				projectile_img=bullet_type[1];
				projectile_dir=Game.Direction.Left;
			}
		}
		/*Case Right*/
		if(y==hero.getY() && x<hero.getX()){
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
