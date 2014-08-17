package utility;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.Inventory;
import main.Main;
import main.Main.GameState;

public class InventoryInput implements KeyListener{
	public boolean canMoveSelector;
	private Stopwatch timer;
	public static int currentRowIndex=0,currentColIndex=0;
	public InventoryInput(){
		timer=new Stopwatch();
		timer.start();
		Input.resetInput();
		canMoveSelector=true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_ENTER: Main.frame.setContentPane(Main.drawPane);
								Main.gameStatus=GameState.Normal;
								Main.drawPane.requestFocusInWindow();
								break;
		case KeyEvent.VK_A:		if(canMoveSelector){
									Inventory.selector.tryMoveLeft();
									canMoveSelector=false;
								}
								break;
		case KeyEvent.VK_W:		if(canMoveSelector){
									Inventory.selector.tryMoveUp();
									canMoveSelector=false;
								}			
								break;
		case KeyEvent.VK_S:		if(canMoveSelector){
									Inventory.selector.tryMoveDown();
									canMoveSelector=false;
								}			
								break;
		case KeyEvent.VK_D:		if(canMoveSelector){
									Inventory.selector.tryMoveRight();
									canMoveSelector=false;
									timer.reset();
								}			
								break;
		case KeyEvent.VK_SPACE: Inventory.selector.itemCollision();
								break;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_A:		canMoveSelector=true;			
								break;
		case KeyEvent.VK_W:		canMoveSelector=true;			
								break;
		case KeyEvent.VK_S:		canMoveSelector=true;				
								break;
		case KeyEvent.VK_D:		canMoveSelector=true;			
								break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
