import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Level {
	static ArrayList<Block> map=new ArrayList<Block>();
	static ArrayList<Monster> monsterlist=new ArrayList<Monster>();
	static ArrayList<Tile> tile=new ArrayList<Tile>();
	static ArrayList<Polygon> poly=new ArrayList<Polygon>();
	public Level(){
		monsterlist.add(new Monster(108,336,1));
		map.add(new Block(192,144,9));
		map.add(new Block(216,144,2));
		map.add(new Block(240,120,11));
		map.add(new Block(240,144,2));
		map.add(new Block(264,144,2));
		map.add(new Block(288,144,10));	

		map.add(new Block(288,360,2));
		map.add(new Block(192,144,9));
		map.add(new Block(216,144,2));
		map.add(new Block(240,120,11));
		map.add(new Block(240,144,2));
		map.add(new Block(264,144,2));
		map.add(new Block(288,144,10));
		map.add(new Block(312,360,2));
		map.add(new Block(336,360,2));
		map.add(new Block(360,360,2));
		map.add(new Block(384,360,2));
		map.add(new Block(408,360,2));
		map.add(new Block(432,360,2));
		map.add(new Block(456,360,2));
		map.add(new Block(480,360,2));
		
		map.add(new Block(0,384,8));
		map.add(new Block(24,384,8));
		map.add(new Block(48,384,8));
		map.add(new Block(72,384,8));
		map.add(new Block(96,384,8));
		map.add(new Block(120,384,8));
		map.add(new Block(144,384,8));
		map.add(new Block(168,384,8));
		map.add(new Block(192,384,8));
		map.add(new Block(216,384,8));
		map.add(new Block(240,384,8));
		map.add(new Block(264,384,8));
		map.add(new Block(288,384,8));
		map.add(new Block(312,384,8));
		map.add(new Block(336,384,8));
		map.add(new Block(360,384,8));
		map.add(new Block(384,384,8));
		map.add(new Block(408,384,8));
		map.add(new Block(432,384,8));
		map.add(new Block(456,384,8));
		map.add(new Block(480,384,8));
		loadTile();
	}
	public void loadTile(){
		try {
			BufferedImage img=ImageIO.read(getClass().getResource("/tileset/snowHillRight.png"));
			tile.add(new Tile(504,360,48,48,1));
			//tile.add(new Tile(img,504,360,1));
			img=ImageIO.read(getClass().getResource("/tileset/snowHillLeft.png"));
			tile.add(new Tile(624,360,48,48,2));
			img=ImageIO.read(getClass().getResource("/tileset/default.png"));
			tile.add(new Tile(img,0,360,3));
			tile.add(new Tile(img,24,360,3));
			tile.add(new Tile(img,48,360,3));
			tile.add(new Tile(img,72,360,3));
			tile.add(new Tile(img,96,360,3));
			tile.add(new Tile(img,120,360,3));
			tile.add(new Tile(img,144,360,3));
			tile.add(new Tile(img,168,360,3));
			tile.add(new Tile(img,192,360,3));
			tile.add(new Tile(img,216,360,3));
			tile.add(new Tile(img,240,360,3));
			tile.add(new Tile(img,264,360,3));
			tile.add(new Tile(img,288,360,3));
			tile.add(new Tile(img,288,312,3));
			tile.add(new Tile(img,312,360,3));
			tile.add(new Tile(img,336,360,3));
			tile.add(new Tile(img,360,360,3));
			tile.add(new Tile(img,384,360,3));
			tile.add(new Tile(img,408,360,3));
			tile.add(new Tile(img,432,360,3));
			tile.add(new Tile(img,456,360,3));
			tile.add(new Tile(img,480,360,3));
			//tile.add(new Tile(img,504,384,3));
			tile.add(new Tile(img,504,408,3));
			tile.add(new Tile(img,528,408,3));
			tile.add(new Tile(img,552,408,3));
			tile.add(new Tile(img,576,408,3));
			tile.add(new Tile(img,600,408,3));
			tile.add(new Tile(img,624,408,3));
			tile.add(new Tile(img,648,408,3));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void render(Graphics g){
		for(Tile atile:tile)
			g.drawImage(atile.img, atile.x, atile.y, atile.width, atile.height, null);
	}
}
