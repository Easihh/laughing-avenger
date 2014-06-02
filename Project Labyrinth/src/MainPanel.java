import java.awt.Graphics;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Character hero=new Character(48, 48);
	private Level theLevel=new Level();
	/**
	 * Create the panel.
	 */
	public MainPanel() {
		//setBackground(Color.black);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		theLevel.render(g);
		hero.render(g);
	}
}
