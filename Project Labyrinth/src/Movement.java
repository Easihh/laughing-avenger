import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class Movement {
	public Animation walk_down=new Animation();
	public Animation walk_up=new Animation();
	public Animation walk_left=new Animation();
	public Animation walk_right=new Animation();
	private BufferedImage img;
	private int sheet_cols;
	private int sheet_row;
	private int assetSize=32;
	private Vector<BufferedImage> spriteSheet;
	
	public Movement(){
		 try {
		 spriteSheet=getSpriteSheet("Lolo_down");
		 setAnimation(walk_down,spriteSheet);
		 spriteSheet=getSpriteSheet("Lolo_up");
		 setAnimation(walk_up,spriteSheet);
		 spriteSheet=getSpriteSheet("Lolo_left");
		 setAnimation(walk_left,spriteSheet);
		 spriteSheet=getSpriteSheet("Lolo_right");
		 setAnimation(walk_right,spriteSheet);
		} catch (IOException e) {e.printStackTrace();}
	}
	private Vector<BufferedImage> getSpriteSheet(String sourceFile) throws IOException {
		Vector<BufferedImage> sheet=new Vector<BufferedImage>();
		img=ImageIO.read(getClass().getResourceAsStream("/tileset/"+sourceFile+".png"));
		sheet_cols=img.getWidth()/assetSize;
		sheet_row=img.getHeight()/assetSize;
		for(int i=0;i<sheet_row;i++){
			 for(int j=0;j<sheet_cols;j++){
				sheet.add(img.getSubimage(j*assetSize, i*assetSize, assetSize, assetSize));
			 }
		}
		return sheet;
	}
	private void setAnimation(Animation Animation, Vector<BufferedImage> spriteSheet) {
		for(int i=0;i<spriteSheet.size();i++){
			Animation.AddScene(spriteSheet.get(i), 100);
		}
		
	}
	
	public Animation getWalkAnimation(){
		switch(Character.dir){	
			case Left: 	return walk_left;
			case Right:	return walk_right;
			case Up:	return walk_up;
			case Down:	return walk_down;	
		}
		return null;
	}
}
