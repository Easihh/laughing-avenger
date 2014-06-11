import java.awt.Graphics;


public class OneWayArrow  extends Tile{

	public OneWayArrow(int x, int y, int type) {
		super(x, y, type);
		getImage();
	}
	public void getImage(){
		switch(type){
		case 11:	super.img=Level.game_tileset[15]; //up
					break;
		case 12:	super.img=Level.game_tileset[20];//left
					break;
		case 13:	super.img=Level.game_tileset[21];//right
					break;
		case 14:	super.img=Level.game_tileset[28];//down
					break;
		}
	}
	public void render(Graphics g){
		g.drawImage(img,x,y,null);	
	}
}
