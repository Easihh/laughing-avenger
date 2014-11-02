import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* Author Enrico Talbot
 * 
 * This is the class where the possible input in the game is recorded/dealt with.When the Level is
 * first loaded, the game is in pause mode until you press any of the movement key.
 * 
 * The Game currently use WASD movement keys.
 */
public class Input implements KeyListener {
	/* key[0]=="W",key[1]=="D",key[2]=="S",key[3]=="A",key[4]=="Space"*/
	public int[] key=new int[5];
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			key[3]=1;
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
		}
		if(keycode==KeyEvent.VK_D){
			key[1]=1;
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
		}
		if(keycode==KeyEvent.VK_W){
			key[0]=1;
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
		}
		if(keycode==KeyEvent.VK_S){
			key[2]=1;
			if(Labyrinth.GameState==Game.GameState.NotStarted)
				Labyrinth.GameState=Game.GameState.Normal;
		}
		if(keycode==KeyEvent.VK_SPACE){
			key[4]=1;
			Labyrinth.hero.canShot=true;
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
		switch(keycode){
		case KeyEvent.VK_W: 		key[0]=0;
									break;
		case KeyEvent.VK_D: 		key[1]=0;
									break;
		case KeyEvent.VK_S: 		key[2]=0;
									break;
		case KeyEvent.VK_A: 		key[3]=0;
									break;
		case KeyEvent.VK_SPACE: 	key[4]=0;
									Labyrinth.hero.canShot=true;
									break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}


