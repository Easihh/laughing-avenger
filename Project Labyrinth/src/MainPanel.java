import java.awt.Graphics;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	private Level theLevel=new Level();
	private static final long serialVersionUID = 1L;
	
	public static Character hero=new Character(48, 48);

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		theLevel.render(g);
		hero.render(g);
	}
}
