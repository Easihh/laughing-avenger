import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Phantom extends Monster {
	private Animation walk_down;
	private Animation walk_left;
	private Animation walk_right;
	private Animation walk_up;
	private BufferedImage[] walk_down_img;
	private BufferedImage[] walk_up_img;
	private BufferedImage[] walk_left_img;
	private BufferedImage[] walk_right_img;
	public Phantom(int x, int y, int type) {
		super(x, y, type);
		dir=Game.Direction.Down;
		getImage();
	}
	@Override
	public void render(Graphics g) {
		switch(dir){
		case Left:	walk_left.setImage();
					g.drawImage(walk_left.getImage(),x,y,null);
					break;
		case Right:	walk_right.setImage();
					g.drawImage(walk_right.getImage(),x,y,null);
					break;
		case Up:	walk_up.setImage();
					g.drawImage(walk_up.getImage(),x,y,null);
					break;
		case Down:	walk_down.setImage();
					g.drawImage(walk_down.getImage(),x,y,null);
					break;
		}
	}
	@Override
	public void transform() {}//this monster is immune
	public void update(){
		if(Labyrinth.GameState==Game.GameState.Normal)
			move();
		if(Character.beingPushed){
			if(dir==Game.Direction.Down){
				if(!checkCollision(new Rectangle(Character.x, Character.y+32,16,4),new Rectangle(Character.x+16, Character.y+32,16,4))){
					Character.y+=4;
					Character.beingPushed=false;
				}
			}
			if(dir==Game.Direction.Up){
				if(!checkCollision(new Rectangle(Character.x, Character.y-4,16,4),new Rectangle(Character.x+16, Character.y-4,16,4))){
					Character.y-=4;
					Character.beingPushed=false;
				}	
			}
		}
	}
	private void getImage(){
		walk_left=new Animation();
		walk_right=new Animation();
		walk_up=new Animation();
		walk_down=new Animation();
		walk_left_img=new BufferedImage[3];
		walk_right_img=new BufferedImage[3];
		walk_up_img=new BufferedImage[3];
		walk_down_img=new BufferedImage[3];
		BufferedImage img=null;
		try {
			img=ImageIO.read(getClass().getResource("/tileset/phantom_walk_left.png"));
			} catch (IOException e) {e.printStackTrace();}
		//left
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<3;j++){
				walk_left_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		walk_left.AddScene(walk_left_img[0], 250);
		walk_left.AddScene(walk_left_img[1], 250);
		walk_left.AddScene(walk_left_img[2], 250);
		//right
		try {
			img=ImageIO.read(getClass().getResource("/tileset/phantom_walk_right.png"));
			} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<3;j++){
				walk_right_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		walk_right.AddScene(walk_right_img[0], 250);
		walk_right.AddScene(walk_right_img[1], 250);
		walk_right.AddScene(walk_right_img[2], 250);
		///Down
		try {
			img=ImageIO.read(getClass().getResource("/tileset/phantom_walk_down.png"));
			} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<3;j++){
				walk_down_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		walk_down.AddScene(walk_down_img[0], 250);
		walk_down.AddScene(walk_down_img[1], 250);
		walk_down.AddScene(walk_down_img[2], 250);
		//up
		try {
			img=ImageIO.read(getClass().getResource("/tileset/phantom_walk_up.png"));
			} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<3;j++){
				walk_up_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		walk_up.AddScene(walk_up_img[0], 250);
		walk_up.AddScene(walk_up_img[1], 250);
		walk_up.AddScene(walk_up_img[2], 250);
	}
	private void getnewDirection() {
		int direction=0;
		int new_direction=0;
		if(dir==Game.Direction.Down)
			direction=1;
		if(dir==Game.Direction.Left)
			direction=2;
		if(dir==Game.Direction.Right)
			direction=3;
		if(dir==Game.Direction.Up)
			direction=4;
		Random random=new Random();
		do
			new_direction=random.nextInt(5);
		while (new_direction==0 || new_direction==direction);
		if(new_direction==1)
			dir=Game.Direction.Down;
		if(new_direction==2)
			dir=Game.Direction.Left;
		if(new_direction==3)
			dir=Game.Direction.Right;
		if(new_direction==4)
			dir=Game.Direction.Up;
	}
	private void move() {
		switch(dir){
		case Left: 	if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x-1, y,1,16),new Rectangle(x-1, y+16,1,16))){
							getnewDirection();
						}
						else x-=1;
					}else
						walk_left.reset();
					break;
		case Right: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x+32, y,1,16),new Rectangle(x+32, y+16,1,16))){
							getnewDirection();
						}else x+=1;
					}else
							walk_right.reset();
					break;
		case Up: if(!withinHeroDistance()){
					if(checkCollision(new Rectangle(x, y-1,16,1),new Rectangle(x+16, y-1,16,1))){
						getnewDirection();
					}else y-=1;
				}else//within distance
					if(!shape.intersects(new Rectangle(Character.x,Character.y+32,16,4)) && !shape.intersects(
							new Rectangle(Character.x+16,Character.y+32,16,4)))
						y-=4;
				else	
							if(!Character.beingPushed)
								Character.beingPushed=true;
					break;
		case Down: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x, y+32,16,1),new Rectangle(x+16, y+32,16,1))){
							getnewDirection();
						}else y+=1;
					}else
						if(!shape.intersects(new Rectangle(Character.x,Character.y-4,16,4)) && !shape.intersects(
								new Rectangle(Character.x+16,Character.y-4,16,4)))
							y+=4;
						else	
									if(!Character.beingPushed)
										Character.beingPushed=true;							
					break;
				}
		updateMask();
	}
	private boolean withinHeroDistance() {
		switch(dir){
		case Left:	if(x-Character.x<=64 && (Math.abs(y-Character.y)<=Character.step))
						if(x>Character.x)
							return true;
					break;
		case Right:if(x-Character.x>=-64 && (Math.abs(y-Character.y)<=Character.step))
						if(x<Character.x)
							return true;
					break;
		case Down:	if(y<Character.y && (Math.abs(x-Character.x)<=Character.step))
							return true;
					break;
		case Up:	if(y>Character.y && (Math.abs(x-Character.x)<=Character.step))
						return true;
					break;
		}
		return false;
	}
}
