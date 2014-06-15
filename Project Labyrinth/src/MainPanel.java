import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;


public class MainPanel extends JPanel {
	private Level theLevel=new Level();
	private static final long serialVersionUID = 1L;
	JLabel xposition,yposition;
	public static Character hero=new Character(48, 48);
	public MainPanel(){
		setBackground(Color.black);	
	}
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			theLevel.render(g);
			g.setColor(Color.white);
			g.drawString("x:"+Character.x, 544, 64);
			g.drawString("y:"+Character.y, 544, 96);
			//if(Labyrinth.GameState==Game.GameState.Death)
				//Character.Death.render(g);
			hero.render(g);
	}
}
