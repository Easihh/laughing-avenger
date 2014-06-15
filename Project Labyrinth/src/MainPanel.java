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
		setLayout(null);
		
		xposition = new JLabel("New label");
		xposition.setBounds(544, 64, 75, 25);
		add(xposition);
		xposition.setBackground(Color.BLACK);
		xposition.setForeground(Color.WHITE);
		
		yposition = new JLabel("New label");
		yposition.setBounds(544, 96, 75, 25);
		add(yposition);
		yposition.setForeground(Color.WHITE);
		yposition.setBackground(Color.BLACK);
	}
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			theLevel.render(g);
			xposition.setText("X:"+Character.x);
			yposition.setText("Y:"+Character.y);
			if(Labyrinth.GameState==Game.GameState.Death)
				Character.Death.render(g);
			else hero.render(g);
	}
}
