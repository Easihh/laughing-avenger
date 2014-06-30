import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snakey  extends Monster{
	private final long nano=1000000L;
	private Animation SnakeyAnimation;
	private BufferedImage spriteSheet[];
	private boolean boat_movement=false;
	public Snakey(int x, int y, int type) {
		super(x, y, type);
		SnakeyAnimation=new Animation();
		spriteSheet=new BufferedImage[2];
		try {getImage();} catch (IOException e) {e.printStackTrace();}
	}
	public void update(){
		Tile findWaterFlow;
		if(type==Tile.ID.boat.value && !Character.isPushing && boat_movement){
		//	System.out.println("X:"+x);
			//System.out.println("Y:"+y);
			//System.out.println("HeroX:"+Character.getInstance().getX());
			//System.out.println("HeroY:"+Character.getInstance().getY());
			if(WaterDir==Game.Direction.Right){
				//System.out.println("Right");
				if(checkWaterCollision(new Rectangle(x+width,y,1,32))){
					if(Character_is_on_boat())
						Character.getInstance().setX(Character.getInstance().getX()+1);
					x+=1;
				}
				else{
					findWaterFlow=getWaterFlow(new Rectangle(x,y+32+1,32,1));
					if(findWaterFlow.type==Tile.ID.WaterFlowDown.value)
						WaterDir=Game.Direction.Down;						
					}
			}
			else if(WaterDir==Game.Direction.Down){
				//System.out.println("Down");
					if(checkWaterCollision(new Rectangle(x,y+32,32,1))){
						if(Character_is_on_boat())
							Character.getInstance().setY(Character.getInstance().getY()+1);
						y+=1;
					}
					else{
						findWaterFlow=getWaterFlow(new Rectangle(x-1,y,1,32));
						if(findWaterFlow.type==Tile.ID.WaterFlowLeft.value)
							WaterDir=Game.Direction.Left;
						}
			}
			else if(WaterDir==Game.Direction.Left){
				//System.out.println("Left");
					if(checkWaterCollision(new Rectangle(x-1,y,1,32))){
						if(Character_is_on_boat())
							Character.getInstance().setX(Character.getInstance().getX()-1);
						x-=1;
					}
					else{
						findWaterFlow=getWaterFlow(new Rectangle(x,y-1,32,1));
						if(findWaterFlow.type==Tile.ID.WaterFlowUp.value)
							WaterDir=Game.Direction.Up;
						}
			}
			else if(WaterDir==Game.Direction.Up){
				//System.out.println("Up");
							if(checkWaterCollision(new Rectangle(x,y-1,32,1))){
								if(Character_is_on_boat())
									Character.getInstance().setY(Character.getInstance().getY()-1);
								y-=1;
							}
							else{
								findWaterFlow=getWaterFlow(new Rectangle(x+32+1,y,32,32));
								if(findWaterFlow.type==Tile.ID.WaterFlowRight.value)
									WaterDir=Game.Direction.Right;
								}
			}
		}
		updateMask();
		boat_movement=true;
	}
	private boolean Character_is_on_boat() {
		if(Character.getInstance().getX()==x && Character.getInstance().getY()==y)
			return true;
		return false;
	}
	private Tile getWaterFlow(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return Level.map_tile.get(i);
		}
		return null;
	}
	private boolean checkWaterCollision(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return true;
		}
		return false;
	}
	public void render(Graphics g){
		updateAnimation();
		checkState();
		checkifDrown();
		if(TransformedState==0 && !isMovingAcrossScreen){
			g.drawImage(SnakeyAnimation.getImage(), x,y,width,height,null);
		}
		else{	if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null);
		}
	}
	private void checkifDrown() {
		if((System.nanoTime()-time_since_water)/nano>60000 && TransformedState==4 && isDrowning){
			Kill_Respawn();
			System.out.println("X:"+x);
			System.out.println("Y:"+y);
		}
	}
	@Override
	public void transform() {
		previousState=img;
		type=Tile.ID.MoveableBlock.value;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}
	private void Kill_Respawn() {
		Snakey me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	public Snakey  copy(){
		Snakey clone=new Snakey(oldX,oldY,oldtype);
		return clone;
	}
	private void getImage() throws IOException {
		BufferedImage img=null;
		if(type==Tile.ID.RightSnakey.value)
			img=ImageIO.read(getClass().getResourceAsStream("/tileset/worm_right.png"));
		if(type==Tile.ID.LeftSnakey.value)
			img=ImageIO.read(getClass().getResourceAsStream("/tileset/worm_left.png"));
		for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			SnakeyAnimation.AddScene(spriteSheet[0], 1000);
			SnakeyAnimation.AddScene(spriteSheet[1], 1000);
		}
	private boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Kill_Respawn();
			return true;
		}
		return false;
	}
	private void updateAnimation(){
		if(TransformedState==0)
			SnakeyAnimation.setImage();
	}
}
