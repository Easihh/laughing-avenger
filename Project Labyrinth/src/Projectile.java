import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class Projectile {
	private int x;
	private int y;
	private int width=24;
	private int height=24;
	private BufferedImage img=null;	
	private Game.Direction dir;
	private int projectile_speed=4;
	public Polygon shape;
	public Projectile(int x,int y,BufferedImage img,Game.Direction dir){
		this.x=x;
		this.y=y;
		this.img=img;
		this.dir=dir;
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	public void render(Graphics g) {
		switch(dir){
		case Right:	x+=projectile_speed;
					break;
		case Left: 	x-=projectile_speed;
					break;
		case Down: 	y+=projectile_speed;
					break;
		case Up: 	y-=projectile_speed;
					break;
		}
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
		//g.drawPolygon(shape);
		g.drawImage(img,x,y,null);
		//if(x>=Level.map_width-24 || x<=24 || y<=24 || y>=Level.map_height-24){//missle going out of range
			//Character.isShooting=false;			
		//}
		//System.out.println("ProjectTile Y"+y);
	}
}
