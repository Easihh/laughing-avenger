import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Water extends Tile{
	private Animation Water;
	private BufferedImage[] single_img;
	public Water(int x, int y, int type) {
		super(x, y, type);
		Water=new Animation();
		depth=-1;
		getImage();
	}
	public void render(Graphics g){
		Water.setImage();
		g.drawImage(Water.getImage(),x,y,null);
	}
	private void getImage(){
		single_img=new BufferedImage[4];
		BufferedImage img=null;
		try {img=ImageIO.read(getClass().getResource("/tileset/water_sheet.png"));} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<4;j++){
				single_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Water.AddScene(single_img[0], 600);
		Water.AddScene(single_img[1], 300);
		Water.AddScene(single_img[2], 600);
		Water.AddScene(single_img[3], 300);
	}
}
