package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Item {
	public Image img;
	public boolean hasOwnership=false;
	public int x,y;
	public final int width=32,height=32;
	public Rectangle mask;
	public String name;
	public abstract void render(Graphics g);
	public abstract void use();
	public abstract void update();
}
