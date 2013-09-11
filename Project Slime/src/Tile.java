import java.awt.Image;
import java.awt.Polygon;


public class Tile {
	int x,y,width,height;
	Image img;
	Polygon shape;
	int type;
	public Tile(Image img,int x, int y,int type){
		this.img=img;
		this.x=x;
		this.y=y;
		this.type=type;
		this.width=img.getWidth(null);
		this.height=img.getHeight(null);
		build();
	}
	public Tile(int x, int y,int width,int height, int type){
		this.x=x;
		this.y=y;
		this.type=type;
		this.width=width;
		this.height=height;
		build();
	}
	private void build() {
		switch(type){
		case 1: int[] xcoord={x,x+width,x};
				int[] ycoord={y,y+height,y+height};
				shape=new Polygon(xcoord, ycoord, 3);
				break;
				
		case 2: int[] xcoord2={x,x+width,x+width};
				int[] ycoord2={y+height,y,y+height};
				shape=new Polygon(xcoord2, ycoord2, 3);
				break;
		case 3: int[] xcoord3={x,x+width,x+width,x};
				int[] ycoord3={y,y,y+height,y+height};
				shape=new Polygon(xcoord3, ycoord3, 4);
				break;
		}
		Level.poly.add(shape);
	}
}
