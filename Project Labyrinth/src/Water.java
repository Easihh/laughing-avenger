import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Water extends Tile{
	private Animation Water;
	private BufferedImage[] single_img;
	private final int image_cols=4;
	
	public Water(int x, int y, ID type) {
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
		single_img=new BufferedImage[image_cols];
		BufferedImage img=null;
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/water_sheet.png"));} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<image_cols;j++){
				single_img[j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Water.AddScene(single_img[0], 600);
		Water.AddScene(single_img[1], 300);
		Water.AddScene(single_img[2], 600);
		Water.AddScene(single_img[3], 300);
	}
}
