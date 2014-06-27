import java.awt.Graphics;
import java.awt.Rectangle;
public class Phantom extends Monster {
	
	private Movement move;
	public Phantom(int x, int y, int type) {
		super(x, y, type);
		dir=Game.Direction.Down;
		move=new Movement("phantom_walk",250);
	}
	@Override
	public void render(Graphics g) {
		move.getWalkAnimation(dir).setImage();
		g.drawImage(move.getWalkAnimation(dir).getImage(),x,y,null);
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
	private void move() {
		switch(dir){
		case Left: 	if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x-1, y,1,16),new Rectangle(x-1, y+16,1,16))){
							getnewDirection();
						}
						else x-=1;
					}else
						move.walk_left.reset();
					break;
		case Right: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x+32, y,1,16),new Rectangle(x+32, y+16,1,16))){
							getnewDirection();
						}else x+=1;
					}else
							move.walk_right.reset();
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
