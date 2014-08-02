package monster;

import java.awt.Graphics;

public class Merchant extends Monster{

	public Merchant(int x, int y, ID type) {
		super(x, y, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x, y, null);
	}

}
