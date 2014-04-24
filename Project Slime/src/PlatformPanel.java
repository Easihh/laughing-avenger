import java.awt.Dimension;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;

public class PlatformPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JLabel xposition=new JLabel("X:");
	static JLabel yposition=new JLabel("Y:");
	BufferedImage background;
	//CharacterBar mybar=new CharacterBar();
	public PlatformPanel(){	
		xposition.setBounds(0, 0, 50, 20);
		yposition.setBounds(0, 20, 50, 20);
		//mybar.setBounds(0,100,200,16);
		add(xposition);
		add(yposition);
		//add(mybar);
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
		if(Slime.hero.x>Slime.scroll_x && Slime.hero.y<Slime.scroll_y){
		g.translate((int)(Slime.scroll_x-Slime.hero.x), 0);
		xposition.setBounds((int)Slime.hero.x-Slime.scroll_x, Slime.hero.portraitY_size, 50, 20);
		yposition.setBounds((int)Slime.hero.x-Slime.scroll_x, Slime.hero.portraitY_size+20, 50, 20);
		}
		if(Slime.hero.x>Slime.scroll_x && Slime.hero.y>Slime.scroll_y){
			g.translate((int)(Slime.scroll_x-Slime.hero.x), (int)-(Slime.hero.y-Slime.scroll_y));
			xposition.setBounds((int)Slime.hero.x-Slime.scroll_x,(int)(Slime.hero.y-Slime.scroll_y)+Slime.hero.portraitY_size, 50, 20);
			yposition.setBounds((int)Slime.hero.x-Slime.scroll_x, (int)(Slime.hero.y-Slime.scroll_y)+Slime.hero.portraitY_size+20, 50, 20);
		}
		if(Slime.hero.x<Slime.scroll_x && Slime.hero.y<Slime.scroll_y){
			xposition.setBounds(0, Slime.hero.portraitY_size, 50, 20);
			yposition.setBounds(0, Slime.hero.portraitY_size+20, 50, 20);
		}
		if(Slime.hero.x<Slime.scroll_x && Slime.hero.y>Slime.scroll_y){
			g.translate(0, (int)-(Slime.hero.y-Slime.scroll_y));
			xposition.setBounds(0, (int)Slime.hero.y-Slime.scroll_y+Slime.hero.portraitY_size, 50, 20);
			yposition.setBounds(0, (int)Slime.hero.y-Slime.scroll_y+Slime.hero.portraitY_size+20, 50, 20);
		}
		xposition.setText("X:"+(int)Slime.hero.x);
		yposition.setText("Y:"+(int)Slime.hero.y);
		Slime.hero.draw(g);
		//mybar.draw(g);
		Slime.themap.render(g);
		//for(Block ablock:Level.map)
			//ablock.draw(g);
		for(Monster amonster:Level.monsterlist)
			amonster.draw(g);
		//for(Polygon poly:Level.poly){
			//g.drawPolygon(poly);
		//}
	}
}
