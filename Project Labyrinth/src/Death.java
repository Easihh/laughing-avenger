import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;


public class Death {
	int width=32;
	int height=32;
	BufferedImage img;
	BufferedImage[] animation;
	final long nano=1000000L;
	int index=0;
	//Vector<BufferedImage> DeathSprite;
	long time_since_last_animation=0;
	public Death(){
		getSpriteFromSheet();
		time_since_last_animation=System.nanoTime();
	}

	private void getSpriteFromSheet() {
		int maxFrame=4;
		animation=new BufferedImage[50];
		try {img=ImageIO.read(getClass().getResource("/tileset/lolo_death.png"));} catch (IOException e) {e.printStackTrace();}
		 for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<maxFrame;j++){
				animation[(i*maxFrame)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
	}
	public void render(Graphics g){
	if((System.nanoTime()-time_since_last_animation)/nano<200){
			g.drawImage(animation[0], Character.x,Character.y,width,height ,null);
			System.out.println("TEst1");
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=200 && (System.nanoTime()-time_since_last_animation)/nano<400){
		System.out.println("TEst2");
		g.drawImage((Image)animation[1], Character.x,Character.y,width,height ,null);
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=400 && (System.nanoTime()-time_since_last_animation)/nano<600){
		System.out.println("TEst3");
		g.drawImage((Image)animation[2], Character.x,Character.y,width,height ,null);
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=600 && (System.nanoTime()-time_since_last_animation)/nano<800){
		g.drawImage((Image)animation[1], Character.x,Character.y,width,height ,null);
		System.out.println("TEst4");
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=800 && (System.nanoTime()-time_since_last_animation)/nano<1000){
		g.drawImage((Image)animation[2], Character.x,Character.y,width,height ,null);
		System.out.println("TEst5");
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=1000 && (System.nanoTime()-time_since_last_animation)/nano<1200){
		g.drawImage((Image)animation[1], Character.x,Character.y,width,height ,null);
		System.out.println("TEst6");
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>=1200 && (System.nanoTime()-time_since_last_animation)/nano<1400){
		g.drawImage((Image)animation[2], Character.x,Character.y,width,height ,null);
		System.out.println("TEst7");
	}
	else if((System.nanoTime()-time_since_last_animation)/nano>1400 && (System.nanoTime()-time_since_last_animation)/nano<1600){
		g.drawImage((Image)animation[3], Character.x,Character.y,width,height ,null);
		System.out.println("TEst8");
	}
	}
}
