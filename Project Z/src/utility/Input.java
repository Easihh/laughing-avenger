package utility;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.Hero;
import main.Inventory;
import main.Main;
import main.Main.GameState;

public class Input implements KeyListener {
	static public Key[] key;
	public enum Key{W,A,S,D,Space,None,E};
	public Input(){
		key=new Key[6];
		for(int i=0;i<6;i++)
			key[i]=Key.None;
	}
	public static void resetInput(){
		key=new Key[6];
		for(int i=0;i<6;i++)
			key[i]=Key.None;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_W:		key[0]=Key.W;
								break;
		case KeyEvent.VK_D: 	key[1]=Key.D;
								break;	
		case KeyEvent.VK_S:		key[2]=Key.S;
								break;					
		case KeyEvent.VK_A: 	key[3]=Key.A;
								break;
		case KeyEvent.VK_SPACE: key[4]=Key.Space;
								break;	
		case KeyEvent.VK_ENTER: if(Main.gameStatus==GameState.Normal){
									Hero.getInstance().inventory=new Inventory();
									Main.gameStatus=GameState.Menu;
								}
								break;
		case KeyEvent.VK_E:		//key[5]=Key.E;
								if(Hero.getInstance().specialItem!=null)
									Hero.getInstance().specialItem.use();
								break;
						}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_W:		key[0]=Key.None;
								break;
		case KeyEvent.VK_D: 	key[1]=Key.None;
								break;	
		case KeyEvent.VK_S:		key[2]=Key.None;
								break;					
		case KeyEvent.VK_A: 	key[3]=Key.None;
								break;
		case KeyEvent.VK_SPACE: key[4]=Key.None;
								Hero.getInstance().canAttack=true;
								break;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {	
	}
}
