import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
			if(!Character.getInstance().checkIfpressed(Game.button.A)){
				Character.getInstance().keypressed.add(Game.button.A);
			}
		}
		if(keycode==KeyEvent.VK_D){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
				if(!Character.getInstance().checkIfpressed(Game.button.D)){
					Character.getInstance().keypressed.add(Game.button.D);
				}
		}
		if(keycode==KeyEvent.VK_W){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
			if(!Character.getInstance().checkIfpressed(Game.button.W)){
				Character.getInstance().keypressed.add(Game.button.W);
			}
		}
		if(keycode==KeyEvent.VK_S){
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
				if(!Character.getInstance().checkIfpressed(Game.button.S)){
					Character.getInstance().keypressed.add(Game.button.S);
				}
		}
		if(keycode==KeyEvent.VK_SPACE){
			Character.getInstance().canShot=true;
		}
		if(keycode==KeyEvent.VK_Q){
			Labyrinth.GameState=Game.GameState.Death;
			Sound.StageMusic.stop();
			Sound.Death.start();
		}
		if(keycode==KeyEvent.VK_ESCAPE){
			Level.nextLevel();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		Character.getInstance().releaseButton(keycode);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}


