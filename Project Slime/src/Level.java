import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Level {
	private ArrayList<Block> map=new ArrayList<Block>();
	static ArrayList<Monster> monsterlist=new ArrayList<Monster>();
	static ArrayList<Tile> tile=new ArrayList<Tile>();
	private int coordX=0;
	private int coordY=0;
	//static ArrayList<Polygon> poly=new ArrayList<Polygon>();
	public Level(){
		/*XMLOutputFactory outputFactory= XMLOutputFactory.newFactory();
		try{
			FileWriter fileReader= new FileWriter("src/test2.xml");
			XMLStreamWriter reader= outputFactory.createXMLStreamWriter(fileReader);
			reader.writeStartDocument("1.0");
			reader.writeComment("asdas asda");
			reader.writeStartElement("boo");
			reader.writeEndElement();
			reader.close();
		}*/
		XMLInputFactory inputFactory= XMLInputFactory.newFactory();
		try{
			FileReader fileReader= new FileReader("src/testingshit.TMX");
			XMLStreamReader reader= inputFactory.createXMLStreamReader(fileReader);
			while(reader.hasNext()){
				int eventType=reader.getEventType();
				switch (eventType){
				case XMLStreamConstants.START_ELEMENT:
					String elementName= reader.getLocalName();
					if(elementName.equals("tile")){
							createTile(reader.getAttributeValue(0));
							if(coordX==960){
								coordX=0;
								coordY+=24;
							}else coordX+=24;
					}
				}
				reader.next();
			}
			//reader.next();
			reader.close();
		}
		catch (IOException | XMLStreamException e){
			e.printStackTrace();
		}
		//monsterlist.add(new Monster(108,336,1));
		map.add(new Block(192,144,9));
		map.add(new Block(216,144,2));
		map.add(new Block(240,120,11));
		map.add(new Block(240,144,2));
		map.add(new Block(264,144,2));
	//	map.add(new Block(288,144,10));	

		map.add(new Block(288,360,2));
		//map.add(new Block(192,144,9));
		map.add(new Block(216,144,2));
		map.add(new Block(240,120,11));
		map.add(new Block(240,144,2));
		map.add(new Block(264,144,2));
		map.add(new Block(288,144,10));
		
		/*map.add(new Block(312,360,2));
		map.add(new Block(336,360,2));
		map.add(new Block(360,360,2));
		map.add(new Block(384,360,2));
		map.add(new Block(408,360,2));
		map.add(new Block(432,360,2));
		map.add(new Block(456,360,2));
		map.add(new Block(480,360,2));
		
		map.add(new Block(0,384,8));
		map.add(new Block(24,384,8));
		map.add(new Block(48,384,8));
		map.add(new Block(72,384,8));
		map.add(new Block(96,384,8));
		map.add(new Block(120,384,8));
		map.add(new Block(144,384,8));
		map.add(new Block(168,384,8));
		map.add(new Block(192,384,8));
		map.add(new Block(216,384,8));
		map.add(new Block(240,384,8));
		map.add(new Block(264,384,8));
		map.add(new Block(288,384,8));
		map.add(new Block(312,384,8));
		map.add(new Block(336,384,8));
		map.add(new Block(360,384,8));
		map.add(new Block(384,384,8));
		map.add(new Block(408,384,8));
		map.add(new Block(432,384,8));
		map.add(new Block(456,384,8));
		map.add(new Block(480,384,8));
		*/loadTile();
	}
	private void createTile(String attributeValue) throws IOException {
		BufferedImage img=null;
		if(coordX==960){
			coordX=0;
			coordY+=24;
		}
		switch(attributeValue){		
		case "0": break;
		case "1": img=ImageIO.read(getClass().getResource("/tileset/snowCenter.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "2": img=ImageIO.read(getClass().getResource("/tileset/snowCliffLeft.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "3":	img=ImageIO.read(getClass().getResource("/tileset/snowMid.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "4":	img=ImageIO.read(getClass().getResource("/tileset/snowHillLeft2.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "5":	img=ImageIO.read(getClass().getResource("/tileset/buttonRed_pressed.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "6":	img=ImageIO.read(getClass().getResource("/tileset/snowCliffRight.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "7":	img=ImageIO.read(getClass().getResource("/tileset/snowHillLeft.png"));
					tile.add(new Tile(img,coordX,coordY,2));
					break;
		case "8":	img=ImageIO.read(getClass().getResource("/tileset/snowHillRight.png"));
					tile.add(new Tile(img,coordX,coordY,1));
					break;
		case "9":	img=ImageIO.read(getClass().getResource("/tileset/snowHillRight2.png"));
					tile.add(new Tile(img,coordX,coordY,3));
					break;
		case "10":	img=ImageIO.read(getClass().getResource("/tileset/buttonRed.png"));
					tile.add(new Tile(img,coordX,coordY,4));
					break;
		default: break;
		}
	}
	public void loadTile(){
		//try {
			//BufferedImage img=ImageIO.read(getClass().getResource("/tileset/snowHillRight.png"));
			//tile.add(new Tile(504,360,48,48,1));
			//tile.add(new Tile(img,504,360,1));
			//img=ImageIO.read(getClass().getResource("/tileset/snowHillLeft.png"));
			//tile.add(new Tile(624,360,48,48,2));
			/*img=ImageIO.read(getClass().getResource("/tileset/snowMid.png"));
			tile.add(new Tile(img,0,336,3));
			tile.add(new Tile(img,0,360,3));
			tile.add(new Tile(img,24,360,3));
			tile.add(new Tile(img,48,360,3));
			tile.add(new Tile(img,72,360,3));
			tile.add(new Tile(img,96,360,3));
			tile.add(new Tile(img,120,360,3));
			tile.add(new Tile(img,144,360,3));
			tile.add(new Tile(img,168,360,3));
			tile.add(new Tile(img,192,360,3));
			tile.add(new Tile(img,216,360,3));
			tile.add(new Tile(img,240,360,3));
			tile.add(new Tile(img,264,360,3));
			tile.add(new Tile(img,288,360,3));
			tile.add(new Tile(img,288,312,3));
			tile.add(new Tile(img,288,264,3));
			tile.add(new Tile(img,288,216,3));
			tile.add(new Tile(img,288,192,3));
			tile.add(new Tile(img,264,144,3));
			tile.add(new Tile(img,216,144,3));
			tile.add(new Tile(img,240,144,3));
			//tile.add(new Tile(img,216,144,3));
			tile.add(new Tile(img,312,360,3));
			tile.add(new Tile(img,336,360,3));
			tile.add(new Tile(img,360,360,3));
			tile.add(new Tile(img,384,360,3));
			tile.add(new Tile(img,408,360,3));
			tile.add(new Tile(img,432,360,3));
			tile.add(new Tile(img,456,360,3));
			tile.add(new Tile(img,480,360,3));
			tile.add(new Tile(img,504,408,3));
			tile.add(new Tile(img,480,336,3));
			tile.add(new Tile(img,528,408,3));
			tile.add(new Tile(img,552,408,3));
			tile.add(new Tile(img,576,408,3));
			tile.add(new Tile(img,600,408,3));
			tile.add(new Tile(img,624,408,3));
			tile.add(new Tile(img,648,408,3));
			
			img=ImageIO.read(getClass().getResource("/tileset/snowCliffRight.png"));
			tile.add(new Tile(img,288,144,3));
			img=ImageIO.read(getClass().getResource("/tileset/snowCliffLeft.png"));
			tile.add(new Tile(img,192,144,3));
			img=ImageIO.read(getClass().getResource("/tileset/buttonRed.png"));
			tile.add(new Tile(img,240,120,4));*/
	//	} catch (IOException e) {
		//	e.printStackTrace();
	//	}
	}
	public void render(Graphics g){
		for(Tile atile:tile){
			//if(atile.type==1 || atile.type==2)
				//g.drawPolygon(atile.shape);
			 g.drawImage(atile.img, atile.x, atile.y, atile.width, atile.height, null);
		}
		
	}
}
