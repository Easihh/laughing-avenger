import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;


public class Tile {
	 int x;
	 int y;
	private int width=24;
	private int height=24;
	Image img;
	int type;//1=rock 2=moveable green block,3=heartcard,4=closed goal,5=opened chest,99=background
	public Polygon shape;
	public Tile(int x, int y,Image image,int type) {
		this.x=x;
		this.y=y;
		this.type=type;
		img=image;
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	public Tile(Image image) {
		img=image;
		type=99;// we have a background
	}
	public void moveTile(int movement,Game.button abutton){
		switch(abutton){
		case D: 	if(!checkCollison(x+width,y,x+width,y+height-1) && Character.dir==Game.Direction.Right){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
					}
					if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						Character.targetX=Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
						
					}
					if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					break;
		case A: 	if(!checkCollison(x-1,y,x-1,y+height-1)&& Character.dir==Game.Direction.Left){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						Character.targetX=Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					break;
		case W: 	if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					break;
		case S: 	if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						Character.targetX=Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
					break;
		}
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private boolean checkCollison(int x1,int y1,int x2,int y2) {
		// TODO Auto-generated method stub
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)|| aTile.shape.contains(x2,y2)){
				return true;
			}
		}
		return false;
	}
	public boolean isWalkable(){
		return(type==99 || type==4);
	}
	public int getType(){
		return type;
	}
	public void updateMask(){
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		if(type==99){//draw background
			for(int y=0;y<Level.map_height;y+=height){
				for(int x=0;x<Level.map_width;x+=width){
					g.drawImage(img,x,y,width,height,null);
				}
			}
		}
		else
			//g.drawImage(img,x,y,width,height,null);
		g.drawPolygon(shape);
	}

}
