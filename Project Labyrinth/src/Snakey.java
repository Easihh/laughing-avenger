import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snakey  extends Monster{
	private final long nano=1000000L;
	long last_animation_update;
	private long time_since_transform;
	Animation SnakeyAnimation;
	BufferedImage spriteSheet[];
	public Snakey(int x, int y, int type) {
		super(x, y, type);
		SnakeyAnimation=new Animation();
		spriteSheet=new BufferedImage[2];
		try {getImage();} catch (IOException e) {e.printStackTrace();}
	}
	private void getImage() throws IOException {
		BufferedImage img=null;
		if(super.type==20)
			img=ImageIO.read(getClass().getResource("/tileset/worm_right.png"));
		if(super.type==19)
			img=ImageIO.read(getClass().getResource("/tileset/worm_left.png"));
		for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*maxFrame)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			SnakeyAnimation.AddScene(spriteSheet[0], 1000);
			SnakeyAnimation.AddScene(spriteSheet[1], 1000);
		}
	public void render(Graphics g){
		updateAnimation();
		checkState();
		if(TransformedState==0 && !isMovingAcrossScreen){
			g.drawImage(SnakeyAnimation.getImage(), x,y,width,height,null);
		}
		else{	if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null);
		}
	}
	public boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Snakey aTile=this;
			aTile.x=oldX;
			aTile.y=oldY;
			aTile.type=oldtype;
			aTile.img=previousState;	
			aTile.isMovingAcrossScreen=false;
			aTile.TransformedState=0;
			Level.addRespawn(aTile);
			aTile.updateMask();
			Level.toRemove.add(aTile);//remove the tile if it goes offscreen
			return true;
		}
		return false;
	}
	public void updateAnimation(){
		if(TransformedState==0)
			SnakeyAnimation.setImage();
	}
	@Override
	public void transform() {
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}
	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2){
			TransformedState=0;
			type=1;
			img=previousState;
		}	
	}
}
