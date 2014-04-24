import java.awt.Graphics;
import java.net.*;
import java.util.ArrayList;
public class Map {
	String[][] mapRepresentation;
	private int x;
	private int y;
	Tileblock aBlock;
	ArrayList<Tileblock> mapTile=new ArrayList<Tileblock>();
	public Map(String[][] level){
		x=0;
		y=96;
		mapRepresentation=level;
		createTile(mapRepresentation);
	}
	public ArrayList<Tileblock> getBlock(){
		return mapTile;
	}
	public void createTile(String[][] tileset){
		URL imgURL;
		int rowCount=11;
		int columnCount=16;
		for(int i=0;i<rowCount;i++){
			for(int j=0;j<columnCount;j++){
				if(tileset[i][j]=="A0")
					try{	imgURL=getClass().getResource("/images/GreenTree.gif");
							aBlock=new Tileblock(x,y,imgURL,false);
							mapTile.add(aBlock);
						}catch(Exception e){e.printStackTrace();}
						else if(tileset[i][j]=="A1")
					try{	imgURL=getClass().getResource("/images/worldFloor1.gif");
							aBlock=new Tileblock(x,y,imgURL,true);
							mapTile.add(aBlock);
						}catch(Exception e){e.printStackTrace();}		
				x+=32;
			}
			x=0;
			y+=32;
		}
		}
	public void draw(Graphics g){
		for(Tileblock aTile:mapTile)
			aTile.draw(g);
		}
	}
