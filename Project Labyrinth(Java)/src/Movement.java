import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
/*** 
 * This class builds the movement animation for each object that moves.
 */
public class Movement {
	/** The Animation representing walking down*/
	public Animation walk_down=new Animation();
	/** The Animation representing walking up*/
	public Animation walk_up=new Animation();
	/** The Animation representing walking left*/
	public Animation walk_left=new Animation();
	/** The Animation representing walking right*/
	public Animation walk_right=new Animation();
	private BufferedImage img;
	private int sheet_cols,sheet_row,assetSize=32;
	private Vector<BufferedImage> spriteSheet;
	
	/** This class takes an Image that contains all the movement of the object.
	 * Each direction is a row in the source Image sorted from up->Down->Left->Right
	 */
	public Movement(String source,int delay){
		 try {spriteSheet=getSpriteSheet(source);
		 	buildAnimation(delay);
		 	} catch (IOException e) {e.printStackTrace();}
	}
	private void buildAnimation(int delay) {
		for(int i=0;i<(sheet_cols);i++)
			walk_up.AddScene(spriteSheet.get(i), delay);
		for(int i=sheet_cols;i<2*sheet_cols;i++)
			walk_down.AddScene(spriteSheet.get(i), delay);
		for(int i=2*sheet_cols;i<3*sheet_cols;i++)
			walk_left.AddScene(spriteSheet.get(i), delay);
		for(int i=3*sheet_cols;i<spriteSheet.size();i++)
			walk_right.AddScene(spriteSheet.get(i), delay);
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
	/*** Return the Correct animation object depending on the direction of the Character*/
	public Animation getWalkAnimation(Game.Direction dir){
		switch(dir){	
			case Left: 	return walk_left;
			case Right:	return walk_right;
			case Up:	return walk_up;
			case Down:	return walk_down;
			default:		break;	
		}
		return null;
	}
}
