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
		Character hero=Character.getInstance();
		if(Labyrinth.GameState==Game.GameState.Normal)
			move();
		if(Character.beingPushed){
			if(dir==Game.Direction.Down){
				if(!checkCollision(new Rectangle(hero.getX(), hero.getY()+32,16,4),new Rectangle(hero.getX()+16, hero.getY()+32,16,4))){
					hero.setY(hero.getY()+4);
					Character.beingPushed=false;
				}
			}
			if(dir==Game.Direction.Up){
				if(!checkCollision(new Rectangle(hero.getX(), hero.getY()-4,16,4),new Rectangle(hero.getX()+16, hero.getY()-4,16,4))){
					hero.setY(hero.getY()-4);
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
					if(!shape.intersects(new Rectangle(Character.getInstance().getX(),Character.getInstance().getY()+32,16,4)) && !shape.intersects(
							new Rectangle(Character.getInstance().getX()+16,Character.getInstance().getY()+32,16,4)))
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
						if(!shape.intersects(new Rectangle(Character.getInstance().getX(),Character.getInstance().getY()-4,16,4)) && !shape.intersects(
								new Rectangle(Character.getInstance().getX()+16,Character.getInstance().getY()-4,16,4)))
							y+=4;
						else	
									if(!Character.beingPushed)
										Character.beingPushed=true;							
					break;
				}
		updateMask();
	}
	private boolean withinHeroDistance() {
		Character hero=Character.getInstance();
		switch(dir){
		case Left:	if(x-hero.getX()<=64 && (Math.abs(y-hero.getY())<=hero.step))
						if(x>hero.getX())
							return true;
					break;
		case Right:if(x-hero.getX()>=-64 && (Math.abs(y-hero.getY())<=hero.step))
						if(x<hero.getX())
							return true;
					break;
		case Down:	if(y<hero.getY() && (Math.abs(x-hero.getX())<=hero.step))
							return true;
					break;
		case Up:	if(y>hero.getY() && (Math.abs(x-hero.getX())<=hero.step))
						return true;
					break;
		}
		return false;
	}
}
