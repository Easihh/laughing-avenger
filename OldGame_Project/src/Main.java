import java.net.URL;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Main extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long lastLoopTime;
	static private int fps=0;
	//private Graphics g;
	private boolean gameRunning=true;
	private Character link;
	private final int target_Fps = 60;
	private final long nano = 1000000L;
	public static MainScreen theScreen;
	public final int screenHeight=480;
	public final int screenWidth=512;
	public final int movement_Step=4;
	public final int min_Distance=0;
	public static boolean inMenu=false;
	public long last_keypressed;
	private long lastMonsterUpdate=System.nanoTime();
	//Tileblock ablock=new Tileblock(200,200);
	Mainbar Mainbar=new Mainbar();
	Octopus aMonster=new Octopus(160,160);
	HashMap<Integer,Monster> monsterArray=new LinkedHashMap<Integer,Monster>();
	ArrayList<Integer> monsterDeleteID=new ArrayList<Integer>();
	String[][] Map1=new String[][]{
			{"A0","A1","A0","A1","A1","A1","A0","A1","A0","A1","A0","A0","A1","A0","A0","A0"},
			{"A0","A1","A0","A1","A1","A1","A0","A1","A0","A1","A0","A0","A1","A0","A0","A0"},
			{"A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A0","A0"},
			{"A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A0","A1","A1","A1","A1","A0"},
			{"A1","A1","A0","A1","A1","A1","A0","A1","A0","A1","A1","A1","A1","A1","A1","A1"},
			{"A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A0","A1","A1","A1","A1","A1"},
			{"A1","A1","A0","A1","A1","A1","A0","A1","A0","A1","A1","A1","A1","A1","A1","A1"},
			{"A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A0","A1","A1","A1","A1","A0"},
			{"A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A1","A0","A0"},
			{"A0","A0","A0","A0","A0","A0","A0","A0","A0","A0","A0","A1","A1","A1","A0","A0"},
			{"A0","A0","A0","A0","A0","A0","A0","A0","A0","A0","A0","A1","A1","A1","A0","A0"}};
	String [][] Map2=new String[][]{
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","A1","A1","A1","A1","","","","","","","",""},
			{"","","","","A1","A1","A1","A1","","","","","","","",""},
			{"","","","","A1","A1","A1","A1","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","",""}};
	Map StartArea=new Map(Map1);
	Map area2=new Map(Map2);
	Map currentMap=StartArea;
	ArrayList<Map> WorldMap=new ArrayList<Map>();
	public class Mainbar extends JPanel{
		URL imgURL;
		BufferedImage heart;
		BufferedImage deadheart;
		BufferedImage itemSlot;
		BufferedImage weaponSlot;
		Mainbar(){
			setSize(512,96);
			setBackground(Color.black);
			try{
				imgURL=getClass().getResource("/images/life_heart.gif");
				heart=ImageIO.read(imgURL);
			}catch(Exception e){e.printStackTrace();}
			try{
				imgURL=getClass().getResource("/images/dead_heart.gif");
				deadheart=ImageIO.read(imgURL);
			}catch(Exception e){e.printStackTrace();}
		try{
			imgURL=getClass().getResource("/images/ItemSlot.gif");
			itemSlot=ImageIO.read(imgURL);
		}catch(Exception e){e.printStackTrace();}
		try{
			imgURL=getClass().getResource("/images/basic_sword_up.gif");
			weaponSlot=ImageIO.read(imgURL);
		}catch(Exception e){e.printStackTrace();}
	}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int x=300;
			int y=32;
			int fullHeart=link.getHP()/4;
				for(int i=0;i<fullHeart;i++){
					g.drawImage((Image)heart, x, y,null);
					x+=18;
					}
			int deadHeart=(link.getMaxHP()-link.getHP())/4;
				for(int i=0;i<deadHeart;i++){
					g.drawImage((Image)deadheart, x, y,null);
					x+=18;
				}
			g.setColor(Color.red);
			g.drawString("- L I F E -", 320, 16);
			g.setColor(Color.white);
			g.drawString("R", 260, 20);
			g.drawImage(weaponSlot,254,30 , null);
			g.drawImage(itemSlot,240,20 , null);
			g.drawImage(itemSlot,180,20 , null);
			if(link.getItemSelectedImage()!=null)
				g.drawImage(link.getItemSelectedImage(),188,30,null);
		}
	}
	class MainScreen extends JPanel{
		MainScreen(){
			super();
			setIgnoreRepaint(true);
			setFocusable(true);
			//setBounds(0,320,screenWidth,screenHeight);
			setVisible(true);
			setBackground(Color.white);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//g=getBufferStrategy().getDrawGraphics();	
			currentMap.draw(g);
			link.draw(g);
			for(Monster aMonster:monsterArray.values())
				aMonster.draw(g);
			//System.out.println("Focus"+KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
			//g.dispose();
			//getBufferStrategy().show();	
		}
	}
	Main(){
		super();
		WorldMap.add(StartArea);
		WorldMap.add(area2);
		last_keypressed=0;
		monsterArray.put(aMonster.getID(),aMonster);
		aMonster=new Octopus(192,192);
		monsterArray.put(aMonster.getID(),aMonster);
		aMonster=new Octopus(256,256);
		monsterArray.put(aMonster.getID(),aMonster);
		aMonster=new Octopus(384,416);
		monsterArray.put(aMonster.getID(),aMonster);
		theScreen=new MainScreen();
		add(Mainbar);
		add(theScreen);
		theScreen.addKeyListener(new KeyAdapter() {
			public  void keyPressed(KeyEvent e){
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_S && link.canMove())
					link.setDirection(90);
				if(key==KeyEvent.VK_A && link.canMove())
					link.setDirection(180);
				if(key==KeyEvent.VK_W && link.canMove())
					link.setDirection(270);
				if(key==KeyEvent.VK_D && link.canMove())
					link.setDirection(360);
				if(key==KeyEvent.VK_S || key==KeyEvent.VK_A || key==KeyEvent.VK_W || key==KeyEvent.VK_D){
					if(link.canMove()){
							link.move();
					}
					
					for(Tileblock Tileset:currentMap.getBlock())
						//if(Tileset.getMask()!=null)
					if(link.getMask().intersects(Tileset.getMask())){
						if(link.getDirection()==90)					
							link.setY(link.getY()-(int)(link.getMask().getMaxY()-Tileset.getMask().getMinY()-(movement_Step-min_Distance)));
						if(link.getDirection()==180)
							link.setX(link.getX()-(int)(link.getMask().getMinX()-Tileset.getMask().getMaxX()+(movement_Step-min_Distance)));
						if(link.getDirection()==270)
							link.setY(link.getY()-(int)(link.getMask().getMinY()-Tileset.getMask().getMaxY()+(movement_Step-min_Distance)));
						if(link.getDirection()==360)
							link.setX(link.getX()-(int)(link.getMask().getMaxX()-Tileset.getMask().getMinX()-(movement_Step-min_Distance)));
						link.getMask().setRect(link.getX(), link.getY(), 32, 32);
						}
					//theScreen.repaint();
				}
				if(key==KeyEvent.VK_ENTER){
					inMenu=true;
					Inventory myInventory=new Inventory(theScreen.getLocationOnScreen(),link);
				}
				if(key==KeyEvent.VK_R){
					if((System.nanoTime()-last_keypressed)/nano>500){
					last_keypressed=System.nanoTime();
					link.attack();
					//System.out.println("SwordMask at:"+link.getSword().getMask().getBounds());
					//System.out.println("Position"+"X:"+link.getX()+" Y:"+link.getY());
					if(link.getSword()!=null){
						for(Monster anMonster:monsterArray.values()){
							if(link.getSword().getMask().intersects(anMonster.getMask())){
								anMonster.setDamage();
								System.out.println("MonsterHit");
									if(anMonster.getHP()<=0)
										monsterDeleteID.add(anMonster.getID());
								}
							}
						}
					}
				}}	
	});
		setTitle("Project X");
		setResizable(false);
		//setIgnoreRepaint(true);
		link=new Character();
		setSize(screenWidth,screenHeight);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//createBufferStrategy(2);
	}
	public void collisionCheck(){
		if(link.getInvincible()){
			if((System.nanoTime()-link.getLastInvincible())/nano>750)
				link.setInvincible(false);
		}
		for(Monster aMonster:monsterArray.values()){
			if(link.getMask().intersects(aMonster.getMask()) && !link.getInvincible()){
				link.setHP(link.getHP()-4);
				link.setInvincible(true);
				link.setLastInvincible(System.nanoTime());
				findClosestBlock();
				}
			}
			//if(link.getHP()<=0)
				//System.out.print("Player is Dead");
	}
	public void findClosestBlock(){
		boolean found=false;
		int maxPushBack=24;
		while(!found){
			if(link.getMask().getMinX()<0 || link.getMask().getMaxX()>screenWidth || 
				link.getMask().getMaxY()>screenHeight || link.getMask().getMinY()<96){
				found=true;
				if(link.getDirection()==270)
					link.setY(link.getY()+maxPushBack);
				if(link.getDirection()==360)
					link.setX(link.getX()-maxPushBack);
				if(link.getDirection()==90)
					link.setY(link.getY()-maxPushBack);
				if(link.getDirection()==180)
					link.setX(link.getX()+maxPushBack);
				}
				if(checkBlockCollisionInDirection())
					found=true;
				else{
					switch(link.getDirection()){
					case 270: link.setMask(link.getX(), link.getMask().getMinY()+1, 32, 32);
							break;
					case 360:link.setMask(link.getMask().getMinX()-1, link.getY(), 32, 32);
							break;
					case 90:  link.setMask(link.getX(), link.getMask().getMinY()-1, 32, 32);
							break;
					case 180: link.setMask(link.getMask().getMinX()+1, link.getY(), 32, 32);
							break;
					}
				}
			}
		link.setMask(link.getX(), link.getY(), 32, 32);
		}
	public boolean checkBlockCollisionInDirection(){
		int step=0;
		int maxPushBack=24;
		int playerSize=32;
		for(Tileblock tileset:currentMap.getBlock()){
			if(link.getMask().intersects(tileset.getMask())){
					switch(link.getDirection()){
					case 270: 	step=Math.abs((int)tileset.getMask().getMinY()-(int)link.getY()-playerSize);
								if(step>maxPushBack)step=maxPushBack;
								link.setY(link.getY()+step);
								break;
					case 360:  	step=Math.abs((int)tileset.getMask().getMaxX()-(int)link.getX());
								System.out.println("STEP"+step);
								if(step>maxPushBack)step=maxPushBack;
								link.setX(link.getX()-step);
								break;
					case 90: 	step=Math.abs((int)link.getY()-(int)tileset.getMask().getMaxY());
								if(step>maxPushBack)step=maxPushBack;
								link.setY(link.getY()-step);
								break;
					case 180:  	step=Math.abs((int)tileset.getMask().getMinX()-(int)link.getX()-playerSize);
								if(step>maxPushBack)step=maxPushBack;
								link.setX(link.getX()+step);
								break;
					}
					return true;
			}
		}
		return false;
	}
	public void deleteMonster(){
		for(int id:monsterDeleteID)
			monsterArray.remove(id);
		monsterDeleteID.removeAll(monsterDeleteID);
	}
	public void monsterAiUpdate(){
		if((System.nanoTime()-lastMonsterUpdate)/nano>=100){
		for(Monster aMonster:monsterArray.values()){
			aMonster.AI();
			lastMonsterUpdate=System.nanoTime();
			for(Tileblock tileset:currentMap.getBlock())
				if(aMonster.getMask().intersects(tileset.getMask()) || aMonster.x>=screenWidth || 
						aMonster.y>=screenHeight-32|| aMonster.x<0 || aMonster.y<96)
					if(aMonster.direction==90)
						aMonster.y-=4;
					else if(aMonster.direction==180)
						aMonster.x+=4;
					else if(aMonster.direction==270)
						aMonster.y+=4;
					else if(aMonster.direction==360)
						aMonster.x-=4;
					aMonster.getMask().setRect(aMonster.x, aMonster.y, 32, 32);
		}
		}
	}
	public void checkMapBound(){
		if(link.getX()<0){
			//currentMap=area2;
			link.setX(screenWidth-64);
		}
		//if(link.getX()>screenWidth)
			//link.setX(64);
	}
	public static void main(String[] args) {
		Main Game=new Main();
		new Thread(Game).start();
	}
	public void run(){
			//int update=0;
			long timer=System.nanoTime();
		   lastLoopTime = System.nanoTime();
		  while(gameRunning){
			  fps++;
			  //update++;
			  //System.out.println("MENU"+inMenu);
			  if(!inMenu){
			  collisionCheck();
			  deleteMonster();
			  monsterAiUpdate();
			  checkMapBound();
			  }
			  while((System.nanoTime()-lastLoopTime)/nano<16){
				  Thread.yield();
			  }
			  lastLoopTime=System.nanoTime();
			  repaint();
			  if ((System.nanoTime()-timer)/nano>=1000){
				  timer=System.nanoTime();
				  System.out.println("LINK X:"+link.getX()+"\nLINK Y:"+link.getY());
				  setTitle("FPS:"+fps);
				  fps=0;
				  //update=0;
				//System.out.println("Focus"+KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
			  }
		  }
	}

}
