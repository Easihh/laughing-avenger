import java.awt.Graphics;


public class OneWayArrow  extends Tile{

	public OneWayArrow(int x, int y, int type) {
		super(x, y, type);
		isSolid=false;
		getImage();
	}
	public void render(Graphics g){
		g.drawImage(img,x,y,null);	
	}
	private void getImage(){
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
	public boolean checkArrow() {
			if(Character.dir==Game.Direction.Down && getType()==11){
				if(shape.contains(Character.x,Character.y+height-1) || shape.contains(Character.x+width,Character.y+height-1))
					return false;//allow pass
				return true; 
			}
			if(Character.dir==Game.Direction.Up && getType()==14){
				if(shape.contains(Character.x,Character.y) || shape.contains(Character.x+width,Character.y))
					return false;
				return true;
			}
			if(Character.dir==Game.Direction.Left && getType()==13){
				if(shape.contains(Character.x,Character.y) || shape.contains(Character.x,Character.y+height))
					return false;
				return true;
			}
			if(Character.dir==Game.Direction.Right && getType()==12){
				if(shape.contains(Character.x+width-1,Character.y) ||shape.contains(Character.x+width-1,Character.y+height-1))
					return false;
				return true;
			}
			return false;
	}
}
