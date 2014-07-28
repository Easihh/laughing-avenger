package drawing;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;

public class DrawPanel  extends JPanel{	
	private static final long serialVersionUID = -7489812449966798249L;
	private Mainbar mainBar;
	private GameDrawPanel gameDraw;
	public DrawPanel(){
		gameDraw=new GameDrawPanel();
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		setSize(512,416);
		setBackground(Color.BLACK);
		mainBar=new Mainbar();
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
