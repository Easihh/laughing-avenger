import java.awt.Graphics;
import java.awt.Rectangle;
/* Author Enrico Talbot
 * 
 * This class is the representation of the Ghost-like Created in Game.It has a push Hero ability.
 */
public class Phantom extends Monster {
	private Movement move;
	private final int step=1;//Speed at which the monster moves
	private final int pushStep=4;//extends to which this Monster push the Hero.
	private final int detectDistance=64;//how far the monster can detect the Hero.	
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
	public void transform() {}//this monster is immune to Hero's bullet
	public void update(){
		Character hero=Labyrinth.hero;
		if(Labyrinth.GameState==Game.GameState.Normal)
			move();
		if(hero.beingPushed){
			/* Monster will keep pushing  for as long as its within the collision range with
			 * The Hero.
			 */
			if(dir==Game.Direction.Down){
				if(!checkCollision(new Rectangle(hero.x, hero.y+height,half_width,pushStep),
						new Rectangle(hero.x+half_width, hero.y+height,half_width,pushStep))){
					hero.y=(hero.y+pushStep);
					hero.beingPushed=false;
				}
			}
			if(dir==Game.Direction.Up){
				if(!checkCollision(new Rectangle(hero.x, hero.y-pushStep,half_width,pushStep),
						new Rectangle(hero.x+half_width, hero.y-pushStep,half_width,pushStep))){
					hero.y=(hero.y-pushStep);
					hero.beingPushed=false;
				}	
			}
		}
	}
	/* Phantom Monster should stop moving when they are facing left or right if the Hero
	 * is within the detect range on the X axis.Phantom Monster facing up or down
	 * will try to move toward/push the Hero regardless of distance as long as their X values
	 * are the same.Phantom Monster that collide with other objects will move to a new random
	 * direction until Hero comes within its detect range again.
	 */
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
					if(!shape.intersects(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y+height,half_width,pushStep)) 
							&& !shape.intersects(new Rectangle(Labyrinth.hero.x+half_width,Labyrinth.hero.y+height,half_width,pushStep)))
						y-=pushStep;
				else	
							if(!Labyrinth.hero.beingPushed)
								Labyrinth.hero.beingPushed=true;
					break;
		case Down: if(!withinHeroDistance()){
						if(checkCollision(new Rectangle(x, y+height,half_width,step),
								new Rectangle(x+half_width, y+height,half_width,step))){
							getnewDirection();
						}else y+=step;
					}else
						if(!shape.intersects(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y-pushStep,half_width,pushStep)) 
								&& !shape.intersects(new Rectangle(Labyrinth.hero.x+half_width,Labyrinth.hero.y-pushStep,half_width,pushStep)))
							y+=pushStep;
						else	
									if(!Labyrinth.hero.beingPushed)
										Labyrinth.hero.beingPushed=true;							
					break;
		default:
			break;
				}
		updateMask();
	}
	/* This tells if the Hero is within this Monster detect range.Phantom type Monster should
	 * only be able to detect hero when its within detectDistance when the Monster is 
	 * facing left or right but should be able to detect the hero if facing up or down
	 * regardless of the distance as long as the Monster's X and Hero's X are the same.
	 */
	private boolean withinHeroDistance() {
		Character hero=Labyrinth.hero;
		switch(dir){
		case Left:	if(x-hero.x<=detectDistance && (Math.abs(y-hero.y)<=hero.step))
						if(x>hero.x)
							return true;
					break;
		case Right:if(x-hero.x>=-detectDistance && (Math.abs(y-hero.y)<=hero.step))
						if(x<hero.x)
							return true;
					break;
		case Down:	if(y<hero.y && (Math.abs(x-hero.x)<=hero.step))
							return true;
					break;
		case Up:	if(y>hero.y && (Math.abs(x-hero.x)<=hero.step))
						return true;
					break;
		default:
			break;
		}
		return false;
	}
}
