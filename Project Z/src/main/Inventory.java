package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import utility.InventoryInput;
import drawing.Mainbar;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class Inventory extends JPanel{
	private Mainbar mainBar;
	private int[] T1xpoints={192,224,256},T2xpoints={256,288,320},T3xpoints={320,352,384},T4xpoints={224,256,288},T5xpoints={288,320,352},
			T6xpoints={224,256,288},T7xpoints={288,320,352},T8xpoints={256,288,320},T9xpoints={256,288,320},
			T1ypoints={464,400,464},T2ypoints={464,400,464},T3ypoints={464,400,464},T4ypoints={400,464,400},T5ypoints={400,464,400},
			T6ypoints={400,336,400},T7ypoints={400,336,400},T8ypoints={336,400,336},T9ypoints={336,272,336};
	public static Selector selector;
	private static final long serialVersionUID = 1L;
		public Inventory(){
			selector=new Selector();
			setForeground(Color.WHITE);		
			setPreferredSize(new Dimension(512,640));
			setSize(new Dimension(512,640));
			setBackground(Color.BLACK);
			setLayout(new BorderLayout(0, 0));
			mainBar=new Mainbar();
			FlowLayout flowLayout = (FlowLayout) mainBar.getLayout();
			flowLayout.setVgap(0);
			flowLayout.setHgap(0);
			add(mainBar, BorderLayout.SOUTH);
			setFocusable(true);
			addKeyListener(new InventoryInput());
			Main.frame.setContentPane(this);
			requestFocusInWindow();
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(selector.mySelect, selector.x, selector.y, null);
			g.setColor(Color.red);
			g.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
			g.drawString("INVENTORY", 96, 72);
			g.setColor(Color.magenta);
			g.drawRect(112, 96, 48, 48);
			g.drawRect(240, 97, 192, 97);
			drawItem(g);
			drawTriforce(g);
		}
		private void drawTriforce(Graphics g) {
			g.setColor(Color.red);
			g.drawPolygon(T1xpoints,T1ypoints,3);
			g.drawPolygon(T2xpoints,T2ypoints,3);
			g.drawPolygon(T3xpoints,T3ypoints,3);
			g.drawPolygon(T4xpoints,T4ypoints,3);
			g.drawPolygon(T5xpoints,T5ypoints,3);
			g.drawPolygon(T6xpoints,T6ypoints,3);
			g.drawPolygon(T7xpoints,T7ypoints,3);
			g.drawPolygon(T8xpoints,T8ypoints,3);
			//g.drawPolygon(T9xpoints,T9ypoints,3);
			g.setColor(Color.yellow);
			g.fillPolygon(T9xpoints, T9ypoints, 3);
		}
		private void drawItem(Graphics g) {			
			Hero hero=Hero.getInstance();
			for(int i=0;i<hero.inventoryRow;i++){
				for(int j=0;j<hero.inventoryCol;j++){
					if(hero.inventory_items[i][j]!=null)
						if(hero.inventory_items[i][j].hasOwnership)
							g.drawImage(hero.inventory_items[i][j].img, hero.inventory_items[i][j].inventoryX, hero.inventory_items[i][j].InventoryY,null);
				}	
			}
		}
}
