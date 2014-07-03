import java.awt.Graphics;
import java.awt.Rectangle;
public class Phantom extends Monster {
	private Movement move;
	private final int step=1;
	private final int pushStep=4;
	private final int detectDistance=64;	
	public Phantom(int x, int y, ID type) {
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
				if(!checkCollision(new Rectangle(hero.getX(), hero.getY()+height,half_width,pushStep),
						new Rectangle(hero.getX()+half_width, hero.getY()+height,half_width,pushStep))){
					hero.setY(hero.getY()+pushStep);
					Character.beingPushed=false;
				}
			}
			if(dir==Game.Direction.Up){
				if(!checkCollision(new Rectangle(hero.getX(), hero.getY()-pushStep,half_width,pushStep),
						new Rectangle(hero.getX()+half_width, hero.getY()-pushStep,half_width,pushStep))){
					hero.setY(hero.getY()-pushStep);
					Character.beingPushed=false;
				}	
			}
		}
	}
	private void move() {
		switch(dir){
		case Left: 	if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x-step, y,step,half_height),
								new Rectangle(x-step, y+half_height,step,half_height))){
							getnewDirection();
						}
						else x-=step;
					}else
						move.walk_left.reset();
					break;
		case Right: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x+width, y,step,half_height),
								new Rectangle(x+width, y+half_height,step,half_width))){
							getnewDirection();
						}else x+=step;
					}else
							move.walk_right.reset();
					break;
		case Up: if(!withinHeroDistance()){
					if(checkCollision(new Rectangle(x, y-step,half_width,step),
							new Rectangle(x+half_width, y-step,half_width,step))){
						getnewDirection();
					}else y-=step;
				}else
					if(!shape.intersects(new Rectangle(Character.getInstance().getX(),Character.getInstance().getY()+height,half_width,pushStep)) 
							&& !shape.intersects(new Rectangle(Character.getInstance().getX()+half_width,Character.getInstance().getY()+height,half_width,pushStep)))
						y-=pushStep;
				else	
							if(!Character.beingPushed)
								Character.beingPushed=true;
					break;
		case Down: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x, y+height,half_width,step),
								new Rectangle(x+half_width, y+height,half_width,step))){
							getnewDirection();
						}else y+=step;
					}else
						if(!shape.intersects(new Rectangle(Character.getInstance().getX(),Character.getInstance().getY()-pushStep,half_width,pushStep)) 
								&& !shape.intersects(new Rectangle(Character.getInstance().getX()+half_width,Character.getInstance().getY()-pushStep,half_width,pushStep)))
							y+=pushStep;
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
		case Left:	if(x-hero.getX()<=detectDistance && (Math.abs(y-hero.getY())<=hero.step))
						if(x>hero.getX())
							return true;
					break;
		case Right:if(x-hero.getX()>=-detectDistance && (Math.abs(y-hero.getY())<=hero.step))
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
