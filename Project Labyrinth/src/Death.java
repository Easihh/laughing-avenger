import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Death {
	private final long nano=1000000L;
	private final int  width=32;
	private final int height=32;
	private Animation Death;
	private BufferedImage img;
	private BufferedImage[] animation;
	public boolean isDone=false;
	
	public Death(){
		Death=new Animation();
		getSpriteFromSheet();
	}
	
	public void render(Graphics g){
		isDone();
		if(!isDone){
			Death.setImage();
			g.drawImage(Death.getImage(),Character.x,Character.y,width,height,null);
		}
	}
	
	private void getSpriteFromSheet() {
		int maxFrame=4;
		animation=new BufferedImage[50];
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/lolo_death.png"));} catch (IOException e) {e.printStackTrace();}
		 for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<maxFrame;j++){
				animation[(i*maxFrame)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		 Death.AddScene(animation[0], 500);
		 Death.AddScene(animation[1], 100);
		 Death.AddScene(animation[2], 100);
		 Death.AddScene(animation[1], 100);
		 Death.AddScene(animation[2], 100);
		 Death.AddScene(animation[1], 100);
		 Death.AddScene(animation[2], 100);
		 Death.AddScene(animation[1], 100);
		 Death.AddScene(animation[2], 100);
		 Death.AddScene(animation[3], 500);
	}
	private void isDone(){
		if(Death.index==9){
			if(Death.getSceneCurrentDuration(9)+(System.nanoTime()-Death.last_update)/nano >Death.getSceneMaxDuration(9)){
				isDone=true;
				Level.restart();
			}
		}
	}
}
