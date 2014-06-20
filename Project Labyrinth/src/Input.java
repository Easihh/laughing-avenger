import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
		}
		if(keycode==KeyEvent.VK_ESCAPE){
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


