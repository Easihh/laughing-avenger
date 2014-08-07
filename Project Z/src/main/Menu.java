package main;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.KeyStroke;

import drawing.GameDrawPanel;

import java.awt.event.KeyEvent;

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
		SaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		SaveMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//if("Save".equalsIgnoreCase(e.getActionCommand()))
					save();			
			}
		});
		SaveMenuItem.setForeground(Color.WHITE);
		SaveMenuItem.setBackground(Color.BLACK);
		FileMenu.add(SaveMenuItem);
		
		JMenuItem LoadMenuItem = new JMenuItem("Load");
		LoadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		LoadMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//if("Save".equalsIgnoreCase(e.getActionCommand()))
					load();			
			}
		});
		LoadMenuItem.setForeground(Color.WHITE);
		LoadMenuItem.setBackground(Color.BLACK);
		FileMenu.add(LoadMenuItem);
	}
	private void save() {
		System.out.println("Saving");
		Hero hero=Hero.getInstance();
		XMLOutputFactory outputFactory=XMLOutputFactory.newFactory();
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("saveSlot1.xml");
			XMLStreamWriter writer=outputFactory.createXMLStreamWriter(fileWriter);
			writer.writeStartDocument("1.0");
			writer.writeStartElement("Character");
			writer.writeStartElement("Position");
			writer.writeAttribute("X",""+hero.x);
			writer.writeAttribute("Y",""+hero.y);
			writer.writeAttribute("worldX",""+Map.getInstance().worldX);
			writer.writeAttribute("worldY",""+Map.getInstance().worldY);
			writer.writeEndElement();
			writer.writeStartElement("Item");
			writer.writeEndElement();
			writer.writeEndElement();
			writer.flush();
			writer.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}
	private void load() {
		System.out.println("Loading");
		Hero hero=Hero.getInstance();
		Map map=Map.getInstance();
		XMLInputFactory inputFactory=XMLInputFactory.newFactory();
		try {
			FileReader fileReader=new FileReader("saveSlot1.xml");
			XMLStreamReader reader=inputFactory.createXMLStreamReader(fileReader);
			while(reader.hasNext()){
				int eventType=reader.getEventType();
				switch(eventType){
				case XMLStreamConstants.START_ELEMENT: 	if(reader.getLocalName().equals("Position")){
															hero.x=Integer.parseInt(reader.getAttributeValue(0));
															hero.y=Integer.parseInt(reader.getAttributeValue(1));
															map.worldX=Integer.parseInt(reader.getAttributeValue(2));
															map.worldY=Integer.parseInt(reader.getAttributeValue(3));
															GameDrawPanel.worldTranslateX=-map.worldWidth*map.worldX;
															GameDrawPanel.worldTranslateY=-map.worldHeight*map.worldY;
														}
														break;
				}
				reader.next();
			}
			reader.close();
		} catch (XMLStreamException| IOException e) {e.printStackTrace();}
		
		}
}
