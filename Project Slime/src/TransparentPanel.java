import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import java.awt.FlowLayout;


public class TransparentPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public TransparentPanel(){
		setBackground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}
	public void paintComponent(Graphics g){
		Graphics2D g2d=(Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		super.paintComponent(g2d);
		g2d.dispose();
	}
}
