import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class Game {
	public static Vector<BufferedImage> monsterState;
	public static Vector<BufferedImage> game_tileset;
	public static Vector<BufferedImage>	projectile_img;
	public static BufferedImage background;
	private BufferedImage img=null;
	private int sheet_row;
	private int sheet_cols;
	private final int assetSize=32;
	enum Direction{Up,Down,Left,Right,None};
	enum button{W,A,S,D,None};
	enum GameState{NotStarted,Normal,Paused,Death}
	public Game(){
		try {loadAssets();
			} catch (IOException e) {e.printStackTrace();}
	}
	public void loadAssets() throws IOException{
		monsterState=getSpriteSheet("monster_state");
		game_tileset=getSpriteSheet("game_tileset");
		projectile_img=getSpriteSheet("shoot_sheet");
		background=ImageIO.read(getClass().getResourceAsStream("/tileset/testfloor.png"));
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
}
