import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			Slime.hero.dir=Character.direction.Left;
			Slime.hero.isPressedLeft=true;
		}
		if(keycode==KeyEvent.VK_D){
			Slime.hero.dir=Character.direction.Right;
			Slime.hero.isPressedRight=true;
		}
		if(keycode==KeyEvent.VK_SPACE){
			if(Slime.hero.charState!=Character.state.Jumping && Slime.hero.charState!=Character.state.Falling)
				Slime.hero.jump();
		}
		if(keycode==KeyEvent.VK_ESCAPE){
			if(!Slime.isInMenu){
				Slime.menu = new MenuOption();
				Slime.menu.setBounds((int)Slime.hero.x, (int)Slime.hero.y, 100, 80);
				Slime.contentPane.add(Slime.menu);
				Slime.contentPane.setLayer(Slime.menu, 1);
				Slime.isInMenu=true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode=e.getKeyCode();
		if(keycode==KeyEvent.VK_A){
			if(Slime.hero.dir==Character.direction.Left){
				Slime.hero.charState=Character.state.Standing;
			}
			Slime.hero.isPressedLeft=false;
		}
		if(keycode==KeyEvent.VK_D){
			if(Slime.hero.dir==Character.direction.Right){
				Slime.hero.charState=Character.state.Standing;
			}
			Slime.hero.isPressedRight=false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
