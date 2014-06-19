import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Input implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			if(!Character.checkIfpressed(Game.button.A)){
				Character.keypressed.add(Game.button.A);
			}
		}
		if(keycode==KeyEvent.VK_D){
				if(!Character.checkIfpressed(Game.button.D)){
					Character.keypressed.add(Game.button.D);
				}
		}
		if(keycode==KeyEvent.VK_W){
				if(!Character.checkIfpressed(Game.button.W)){
					Character.keypressed.add(Game.button.W);
				}
		}
		if(keycode==KeyEvent.VK_S){
				if(!Character.checkIfpressed(Game.button.S)){
					Character.keypressed.add(Game.button.S);
				}
		}
		if(keycode==KeyEvent.VK_SPACE){
			boolean check=false;
			if(Character.powerActivated)
				check=Character.checkPower();
			if(!check)
				Character.fireProjectile();
			checkPath();
		}
		if(keycode==KeyEvent.VK_ESCAPE){
		}
	}

	private void checkPath() {
		int TargetX=192;
		int TargetY=192;
		ArrayList<Point> Open=new ArrayList<Point>();
		ArrayList<Point> Close=new ArrayList<Point>();
		Open.add(new Point(Character.x,Character.y));
		int StartX=0;
		int StartY=0;
		int DeltaX=Character.x-256;
		int DeltaY=Character.y-224;
		while(Character.x+StartX!=TargetX){
			if(DeltaX>0){//target is to the left
				//System.out.println("STARTX"+StartX);
				if(!checkCollison(Character.x+StartX-1,Character.y+StartY,Character.x+StartX-1,Character.y+32-1+StartY)){//left
					StartX-=16;
					Open.add(new Point(Character.x+StartX,Character.y+StartY));
				}
				else if(!checkCollison(Character.x+StartX+32-1,Character.y+32+StartY,Character.x+StartX,Character.y+32+StartY)){//down
					StartY+=16;
					Open.add(new Point(Character.x+StartX,Character.y+StartY));
				}
				else if(!checkCollison(Character.x+32-1+StartX,Character.y-1+StartY,Character.x+StartX,Character.y-1+StartY)){//up
					StartY-=16;
					Open.add(new Point(Character.x+StartX,Character.y+StartY));
				}
				//if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
				else StartX-=16;
			}
		}
		for(Point aPoint:Open){
			System.out.println(aPoint);
		}
	}
	public boolean checkCollison(int x1,int y1,int x2,int y2) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)|| aTile.shape.contains(x2,y2)){
				return true;
			}
		}
		return false;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		Character.releaseButton(keycode);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
