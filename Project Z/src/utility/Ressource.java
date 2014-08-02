package utility;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;


public class Ressource {
	public static Vector<BufferedImage> game_tileset;
	public static Vector<BufferedImage> monster_type;
	public static Vector<BufferedImage> weaponType;
	public static BufferedImage obtainItem;
	public static BufferedImage flashSwordDown,flashSwordUp,flashSwordLeft,flashSwordRight;
	public static Vector<BufferedImage> rupee;
	private final int tileSize=32;
	public Ressource(){
		try {loadTileset();
			loadMonster();
			loadWeaponType();
			loadFlashSword();
			loadSound();
			loadObtainItemSpriteSheet();
			loadRupee();
		} catch (IOException e) {
		e.printStackTrace();}
	}

	private void loadRupee() throws IOException {
		rupee=new Vector<BufferedImage>();
		BufferedImage img=ImageIO.read(getClass().getResourceAsStream("/Map/rupee.png"));
		int row=img.getHeight()/tileSize;
		int col=img.getWidth()/16;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				rupee.add(img.getSubimage(j*16, i*tileSize, 16, tileSize));
			}
		}
	}

	private void loadObtainItemSpriteSheet() throws IOException {
		obtainItem=ImageIO.read(getClass().getResourceAsStream("/Map/Link_ObtainItem.png"));
	}

	private void loadMonster() throws IOException {
		monster_type=new Vector<BufferedImage>();
		BufferedImage img=ImageIO.read(getClass().getResourceAsStream("/Map/ObjectTileset.png"));
		int row=img.getHeight()/tileSize;
		int col=img.getWidth()/tileSize;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				monster_type.add(img.getSubimage(j*tileSize, i*tileSize, tileSize, tileSize));
			}
		}	
	}

	private void loadFlashSword() throws IOException {
		flashSwordDown=ImageIO.read(getClass().getResourceAsStream("/Map/sword_thrown_down.png"));
		flashSwordUp=ImageIO.read(getClass().getResourceAsStream("/Map/sword_thrown_up.png"));
		flashSwordLeft=ImageIO.read(getClass().getResourceAsStream("/Map/sword_thrown_left.png"));
		flashSwordRight=ImageIO.read(getClass().getResourceAsStream("/Map/sword_thrown_right.png"));
	}

	private void loadSound() {
		new Sound("overWorld");
		new Sound("sword");
		new Sound("swordCombine");
		new Sound("selector");
		new Sound("candle");
		new Sound("enemyHit");
		new Sound("enemyKill");
		new Sound("newItem");
		new Sound("newInventItem");
		new Sound("linkHurt");
		new Sound("enterShop");
	}

	private void loadWeaponType() throws IOException {
		weaponType=new Vector<BufferedImage>();
		BufferedImage img=ImageIO.read(getClass().getResourceAsStream("/Map/weaponType.png"));
		int col=img.getWidth()/16;
		for(int i=0;i<col;i++)
			weaponType.add(img.getSubimage(i*16, 0, 16, tileSize));
	}

	private void loadTileset() throws IOException {
		game_tileset=new Vector<BufferedImage>();
		BufferedImage img=ImageIO.read(getClass().getResourceAsStream("/Map/GameTileset.png"));
		int row=img.getHeight()/tileSize;
		int col=img.getWidth()/tileSize;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				game_tileset.add(img.getSubimage(j*tileSize, i*tileSize, tileSize, tileSize));
			}
		}
	}
}
