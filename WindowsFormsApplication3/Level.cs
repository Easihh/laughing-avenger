using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Windows.Forms;
using System.Drawing;
namespace ProjectLabyrinth
{
    class Level
    {
        readonly int roomWidth = 512, roomHeight = 512, tileSize = 32;
        int coordX, coordY,room=1;
        static public List<Tile> map_tile = new List<Tile>();
        Tile background;
        public Level() {
            coordX = coordY = 0;
            setBackground();
            try
            {
                XmlTextReader reader = new XmlTextReader("Level/Level" + room + "map.tmx");
                while (reader.Read())
                {
                    switch (reader.NodeType)
                    {
                        case XmlNodeType.Element: if (reader.Name.Equals("tile"))
                            {
                                reader.MoveToNextAttribute();
                                createTile(reader.Value);
                                coordX += tileSize;
                                if (coordX == roomWidth)
                                {
                                    coordX = 0;
                                    coordY += tileSize;
                                }
                            }
                        break;
                    }
                }

            }
            catch(IOException ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.Message);
            }
        }

        private void setBackground()
        {
            Image floor=Properties.Resources.testfloor;
            background = new Tile(floor);

        }

        private void createTile(string type)
        {
            switch (type)
            {
                case "0": //blank Tile
                            break;
                case "1":   map_tile.Add(new Tile(coordX,coordY,Tile.ID.Rock));
                            break;
                case "2": map_tile.Add(new Tile(coordX, coordY, Tile.ID.ClosedDoor));
                            break;
                case "10":  map_tile.Add(new Tile(coordX, coordY, Tile.ID.Tree));
                            break;
                case "11":  map_tile.Add(new Tile(coordX, coordY, Tile.ID.ClosedChest));
                            break;
                case "30":  map_tile.Add(new Tile(coordX, coordY, Tile.ID.RockWall));
                            break; 
            }
        }

        public void render(PaintEventArgs g)
        {
            background.render(g);
            for (int i = 0; i < map_tile.Count; i++)
                map_tile.ElementAt(i).render(g);
        }
    }
}
