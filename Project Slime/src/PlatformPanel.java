import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JButton;


public class PlatformPanel extends JPanel{
	static JLabel xposition=new JLabel("X:");
	static JLabel yposition=new JLabel("Y:");
	BufferedImage background;
	public PlatformPanel(){	
		xposition.setBounds(0, 0, 50, 20);
		yposition.setBounds(0, 20, 50, 20);
		add(xposition);
		add(yposition);
		setPreferredSize(new Dimension(500,500));
		setLayout(null);
		try {
			background=ImageIO.read(getClass().getResource("/tileset/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		g.drawImage(background, 0, 0, Slime.screensize_x, Slime.screensize_y, null);
		//System.out.println("TEST");
		if(Slime.hero.x>Slime.scroll_x && Slime.hero.y<Slime.scroll_y){
		g.translate((int)(Slime.scroll_x-Slime.hero.x), 0);
		xposition.setBounds((int)Slime.hero.x-Slime.scroll_x, 20, 50, 20);
		yposition.setBounds((int)Slime.hero.x-Slime.scroll_x, 40, 50, 20);
		}
		if(Slime.hero.x>Slime.scroll_x && Slime.hero.y>Slime.scroll_y){
			g.translate((int)(Slime.scroll_x-Slime.hero.x), (int)-(Slime.hero.y-Slime.scroll_y));
			xposition.setBounds((int)Slime.hero.x-Slime.scroll_x,(int)(Slime.hero.y-Slime.scroll_y)+20, 50, 20);
			yposition.setBounds((int)Slime.hero.x-Slime.scroll_x, (int)(Slime.hero.y-Slime.scroll_y)+40, 50, 20);
		}
		if(Slime.hero.x<Slime.scroll_x && Slime.hero.y<Slime.scroll_y){
			xposition.setBounds(0, 20, 50, 20);
			yposition.setBounds(0, 40, 50, 20);
		}
		if(Slime.hero.x<Slime.scroll_x && Slime.hero.y>Slime.scroll_y){
			g.translate(0, (int)-(Slime.hero.y-Slime.scroll_y));
			xposition.setBounds(0, (int)Slime.hero.y-Slime.scroll_y+20, 50, 20);
			yposition.setBounds(0, (int)Slime.hero.y-Slime.scroll_y+40, 50, 20);
		}
		xposition.setText("X:"+(int)Slime.hero.x);
		yposition.setText("Y:"+(int)Slime.hero.y);
		Slime.hero.draw(g);
		Slime.themap.render(g);
		for(Block Blocks:Level.map){
			if(Math.abs(Blocks.getX()-Slime.hero.x)<Slime.screensize_x && (Math.abs(Blocks.getY()-Slime.hero.y)<Slime.screensize_y))
				Blocks.draw(g);
		}
		for(Monster amonster:Level.monsterlist)
			amonster.draw(g);
		for(Polygon poly:Level.poly){
			g.drawPolygon(poly);
		}
	}
}
