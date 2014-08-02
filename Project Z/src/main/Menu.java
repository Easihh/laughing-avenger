package main;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar{
	private static final long serialVersionUID = 1L;

	public Menu(){
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(512,32));
		setLayout(new BorderLayout(0, 0));
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setForeground(Color.WHITE);
		menuBar.setBackground(Color.BLACK);
		add(menuBar, BorderLayout.WEST);
		
		JMenu FileMenu = new JMenu("File");
		FileMenu.setBackground(Color.BLACK);
		FileMenu.setForeground(Color.WHITE);
		menuBar.add(FileMenu);
		setBorder(null);
		final JMenuItem SaveMenuItem = new JMenuItem("Save");
		SaveMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if("Save".equalsIgnoreCase(e.getActionCommand()))
					System.out.println("TEST");
				
			}
		});
		SaveMenuItem.setForeground(Color.WHITE);
		SaveMenuItem.setBackground(Color.BLACK);
		FileMenu.add(SaveMenuItem);
		
		JMenuItem LoadMenuItem = new JMenuItem("Load");
		LoadMenuItem.setForeground(Color.WHITE);
		LoadMenuItem.setBackground(Color.BLACK);
		FileMenu.add(LoadMenuItem);
	}
}
