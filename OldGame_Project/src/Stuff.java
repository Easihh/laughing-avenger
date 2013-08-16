import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class Stuff extends JFrame {

	private JPanel contentPane;
	private JPanel content2=new JPanel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Stuff frame = new Stuff();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Stuff() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		contentPane = new JPanel();
		contentPane.setSize(200,200);
		contentPane.setBackground(Color.blue);
		contentPane.setLayout(new GridLayout(16,11));
		content2.setBackground(Color.DARK_GRAY);
		content2.setSize(300,300);
		JLayeredPane layer=new JLayeredPane();
		layer.add(content2,new Integer(-1));
		layer.add(contentPane,new Integer(0));
		add(layer,BorderLayout.CENTER);
		//pack();
	}
	private static class screen extends JPanel{
		JLayeredPane theLayer=new JLayeredPane();
		screen(){
			//theLayer.add(contentPane,new Integer(-1));
			//theLayer.add(content2,new Integer(0));
		}
	
	}

}
