import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.KeyboardFocusManager;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.Component;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dialog.ModalityType;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
public class Inventory extends JDialog{
	Character link;
	InventoryPanel myinventory=new InventoryPanel();
	ArrayList<Item> myInventory=new ArrayList<Item>();
	private final int itemWidth=32;
	private final int itemHeight=32;
	static Selector select=new Selector();
	public Inventory(Point position, final Character hero){
		myInventory=hero.getInventory();
		link=hero;
		this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	           Main.inMenu=false;
	        }
	    });
		setLocation(position);
		setSize(400,400);
		setTitle("Inventory");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        JButton Cancel=new JButton("Cancel");
        Cancel.addKeyListener(new KeyAdapter(){
        	public void keyPressed(KeyEvent e){
        		int key=e.getKeyCode();
        		if(key==KeyEvent.VK_ENTER){
        		Component comp = (Component) e.getSource();
        		Window window = SwingUtilities.windowForComponent(comp);
        		if(window instanceof JDialog){
        			window.dispose();
        			Main.inMenu=false;
        		}
        	}
        		if(key==KeyEvent.VK_D){
        			if(select.getXInt()<310)
        			select.setSelectX(select.getXInt()+itemWidth);
        		}
        		if(key==KeyEvent.VK_T){
        			for(Item anItem:myInventory)
        			if(select.contains(anItem.getMask()))
        				hero.setItemSelect("bow");
        		}
        		if(key==KeyEvent.VK_A){
        			if(select.getXInt()>150)
        			select.setSelectX(select.getXInt()-itemWidth);
        		}
        		if(key==KeyEvent.VK_S){
        			if(select.getYInt()<143){
        			select.setSelectY(select.getYInt()+itemHeight);
        			}
        		}
        		if(key==KeyEvent.VK_W){
        			if(select.getYInt()>79)
        				select.setSelectY(select.getYInt()-itemHeight);
        		}
        		myinventory.repaint();
        	}
        });
        add(Cancel);
		add(myinventory);
		setVisible(true);
		//KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER);
		//InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		//ActionMap actionMap = getRootPane().getActionMap();
		//if(inputMap != null && actionMap != null){
		//inputMap.put(cancelKeyStroke, "cancel");
		//actionMap.put("cancel", new CancelKeyAction());
		//}
	}
private class InventoryPanel extends JPanel{
	//theInventory.add
	URL imgURL;
	BufferedImage img;
	InventoryPanel(){
		//setSize(500,500);
		setBackground(Color.black);
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.blue);
		g.drawRect(150,75, 200, 100);
		select.draw(g);
		for(Item anItem:myInventory){
			if(anItem.getOwnership())
				anItem.draw(g);
		}
		if(link.getItemSelectedImage()!=null)
				g.drawImage(link.getItemSelectedImage(),60,84,32,32,null);
		g.drawString("USE E BUTTON", 25, 150);
		g.drawString("FOR THIS", 45, 162);
		g.setColor(Color.blue);
		g.drawRoundRect(50, 75, 50, 50, 2, 2);
	}
}
}
/*class CancelKeyAction extends AbstractAction{
	public CancelKeyAction(){}
	public void actionPerformed(ActionEvent ae){
		Component comp = (Component) ae.getSource();
		Window window = SwingUtilities.windowForComponent(comp);
		if(window instanceof JDialog){
			window.dispose();
		}
		System.out.println("???");
	}
}*/