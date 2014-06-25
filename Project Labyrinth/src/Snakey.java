import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snakey  extends Monster{
	private final long nano=1000000L;
	private Animation SnakeyAnimation;
	private BufferedImage spriteSheet[];
	private long time_since_transform;
	public Snakey(int x, int y, int type) {
		super(x, y, type);
		SnakeyAnimation=new Animation();
		spriteSheet=new BufferedImage[2];
		try {getImage();} catch (IOException e) {e.printStackTrace();}
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
	@Override
	public void transform() {
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}
	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1 && !isDrowning){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2 && !isDrowning){
			TransformedState=0;
			type=19;
			img=previousState;
		}
		if((System.nanoTime()-time_since_water)/nano>2000 && TransformedState==1 && isDrowning){
			TransformedState=3;
			img=Level.monsterState[2];
		}
		if((System.nanoTime()-time_since_water)/nano>3000 && TransformedState==3 && isDrowning){
			TransformedState=4;
			img=Level.monsterState[3];
		}
		if((System.nanoTime()-time_since_water)/nano>4000 && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
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
		if(super.type==20)
			img=ImageIO.read(getClass().getResource("/tileset/worm_right.png"));
		if(super.type==19)
			img=ImageIO.read(getClass().getResource("/tileset/worm_left.png"));
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
