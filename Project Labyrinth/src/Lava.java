import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lava extends Tile {
	Animation Lava;
	BufferedImage[] single_img;
	public Lava(int x, int y, ID type) {
		super(x, y, type);
		Lava=new Animation();
		getImage();
	}
	public void render(Graphics g){
		Lava.setImage();
		g.drawImage(Lava.getImage(),x,y,null);
	}
	private void getImage(){
		int row=1;
		int col=4;
		single_img=new BufferedImage[row*col];
		BufferedImage img=null;
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/lava.png"));} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				single_img[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Lava.AddScene(single_img[0], 1000);
		Lava.AddScene(single_img[1], 1500);
		Lava.AddScene(single_img[2], 1500);
		Lava.AddScene(single_img[3], 1000);
	}
}
