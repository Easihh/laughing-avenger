import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/* Author Enrico Talbot 
 * 
 * This class Represent how the game should deal with the Hero's Death.
 */
public class Death {
	private final long nano=1000000L;
	private final int  width=32,height=32;
	private Animation Death;
	private BufferedImage img;
	private BufferedImage[] animation;
	public boolean isDone;
	
	public Death(){
		isDone=false;
		Death=new Animation();
		getSpriteFromSheet();
	}
	
	public void render(Graphics g){
		isDone();
		if(!isDone){
			Death.setImage();
			g.drawImage(Death.getImage(),Labyrinth.hero.x,Labyrinth.hero.y,width,height,null);
		}
	}
	
	private void getSpriteFromSheet() {
		int maxRow=1;
		int maxCol=4;
		animation=new BufferedImage[maxRow*maxCol];
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/lolo_death.png"));} catch (IOException e) {e.printStackTrace();}
		 for(int i=0;i<maxRow;i++){
			 for(int j=0;j<maxCol;j++){
				animation[(i*maxCol)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		 //Build the Death Sequence Animation.
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
	/* After the Hero dies, the current level should be restarted.*/
	private void isDone(){
		if(Death.index==Death.getLastIndex()){
			if(Death.getSceneCurrentDuration(Death.getLastIndex())+(System.nanoTime()
					-Death.last_update)/nano >Death.getSceneMaxDuration(Death.getLastIndex())){
				isDone=true;
				Level.restart();
			}
		}
	}
	
	public void play(){
		Sound.StageMusic.stop();
		Sound.Death.start();
		Labyrinth.GameState=Game.GameState.Death;
	}
}
