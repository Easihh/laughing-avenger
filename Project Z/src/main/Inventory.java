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
	Mainbar mainBar;
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
