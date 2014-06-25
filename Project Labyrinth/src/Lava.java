import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Lava extends Tile {
	Animation Lava;
	BufferedImage[] single_img;
	public Lava(int x, int y, int type) {
		super(x, y, type);
		Lava=new Animation();
		getImage();
	}
	public void render(Graphics g){
		Lava.setImage();
		g.drawImage(Lava.getImage(),x,y,null);
	}
	private void getImage(){
		single_img=new BufferedImage[6];
		BufferedImage img=null;
		try {img=ImageIO.read(getClass().getResource("/tileset/lava.png"));} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<6;j++){
				single_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Lava.AddScene(single_img[0], 600);
		Lava.AddScene(single_img[1], 300);
		Lava.AddScene(single_img[2], 300);
		Lava.AddScene(single_img[3], 600);
		Lava.AddScene(single_img[4], 300);
		Lava.AddScene(single_img[5], 300);
	}
}
