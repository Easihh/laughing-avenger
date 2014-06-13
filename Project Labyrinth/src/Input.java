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
			Character.fireProjectile();
		}
		if(keycode==KeyEvent.VK_ESCAPE){
		}
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
