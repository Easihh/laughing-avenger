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
		case 15:	img=Game.game_tileset.get(Tile.ID.OneWayUp.value);
					break;
		case 20:	img=Game.game_tileset.get(Tile.ID.OneWayLeft.value);
					break;
		case 21:	img=Game.game_tileset.get(Tile.ID.OneWayRight.value);
					break;
		case 28:	img=Game.game_tileset.get(Tile.ID.OneWayDown.value);
					break;
		}
	}
	public boolean checkArrow() {
			if(Character.dir==Game.Direction.Down && type==Tile.ID.OneWayUp.value){
				if(shape.contains(Character.x,Character.y+height-1) || shape.contains(Character.x+width,Character.y+height-1))
					return false;//allow pass
				return true; 
			}
			if(Character.dir==Game.Direction.Up && type==Tile.ID.OneWayDown.value){
				if(shape.contains(Character.x,Character.y) || shape.contains(Character.x+width,Character.y))
					return false;
				return true;
			}
			if(Character.dir==Game.Direction.Left && type==Tile.ID.OneWayRight.value){
				if(shape.contains(Character.x,Character.y) || shape.contains(Character.x,Character.y+height))
					return false;
				return true;
			}
			if(Character.dir==Game.Direction.Right && type==Tile.ID.OneWayLeft.value){
				if(shape.contains(Character.x+width-1,Character.y) ||shape.contains(Character.x+width-1,Character.y+height-1))
					return false;
				return true;
			}
			return false;
	}
}
