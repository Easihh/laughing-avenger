import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
/**
 * 
 * This class contains references to certain objects/assets to be used Throughout the program.
 */
public class Game {
	/*** The Images containing the game's Tile*/
	public static Vector<BufferedImage> monsterState,game_tileset,projectile_img;
	/*** The Image containing the game's background*/
	public static BufferedImage background;
	private BufferedImage img=null;
	private int sheet_row,sheet_cols;
	private final int assetSize=32;
	/*** The Game's possible Direction*/
	enum Direction{Up,Down,Left,Right,None};
	/*** Possible valid input */
	enum button{W,A,S,D,None};
	/*** The different Game States*/
	enum GameState{NotStarted,Normal,Paused,Death}
	/*** The different Powers*/
	enum SpecialPower{Hammer,Ladder,ArrowChange,None}
	public Game(){
		try {loadAssets();
			} catch (IOException e) {e.printStackTrace();}
	}
	/*** Load the main game Assets in Memory since they will be used often*/
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
