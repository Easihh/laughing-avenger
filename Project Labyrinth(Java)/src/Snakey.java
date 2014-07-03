import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snakey  extends Monster{
	private final long nano=1000000L;
	private Animation SnakeyAnimation;
	private BufferedImage spriteSheet[];
	public Snakey(int x, int y, ID type) {
		super(x, y, type);
		SnakeyAnimation=new Animation();
		try {getImage();} catch (IOException e) {e.printStackTrace();}
	}
	public void update(){
		updateAnimation();
		checkState();
		checkifDrown();
		if(type==Tile.ID.boat && !Character.isPushing){
			if(boat_movement)boatMovement();
			if(!boat_movement)
				boat_movement=true;
		}
		updateMask();
	}
	public void render(Graphics g){
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
		if((System.nanoTime()-time_since_water)/nano>duration_in_water && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}
	}
	@Override
	public void transform() {
		previousState=img;
		type=Tile.ID.MoveableBlock;
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
		int row=1;
		int col=2;
		BufferedImage img=null;
		spriteSheet=new BufferedImage[row*col];
		if(type==Tile.ID.RightSnakey)
			img=ImageIO.read(getClass().getResourceAsStream("/tileset/worm_right.png"));
		if(type==Tile.ID.LeftSnakey)
			img=ImageIO.read(getClass().getResourceAsStream("/tileset/worm_left.png"));
		for(int i=0;i<row;i++){
				 for(int j=0;j<col;j++){
					spriteSheet[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
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
