package drawing;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.Menu;

public class DrawPanel  extends JPanel{	
	private static final long serialVersionUID = -7489812449966798249L;
	private Mainbar mainBar;
	private GameDrawPanel gameDraw;
	private Menu menu;
	public DrawPanel(){
		gameDraw=new GameDrawPanel();
		//setSize(512,416);
		setBackground(Color.BLACK);
		mainBar=new Mainbar();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		//menu=new Menu();
		//add(menu);
		add(mainBar);
		add(gameDraw);
		setFocusable(true);
		requestFocusInWindow();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
