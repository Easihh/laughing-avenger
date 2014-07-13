using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
namespace ProjectLabyrinth
{
    class Tile
    {
      Image img=null;
      int x, y;
      public enum ID:int{Rock=0,ClosedDoor=1,Tree=9,ClosedChest=10,Floor=99,RockWall=29
        }
      ID type;
        public Tile(int x, int y, ID type)
        {
            this.x = x;
            this.y = y;
            this.type = type;
            img = GameAsset.Game_Tileset.ElementAt((int)type);
        }

        public Tile(Image floor)
        {
            img = floor;
            type = ID.Floor;
        }
        public void update(){
        }

        public void render(PaintEventArgs g)
        {
            if (type == ID.Floor)
            {
                BufferedGraphicsContext context = new BufferedGraphicsContext();
                for(int i=0;i<512;i+=32)
                    for(int j=0;j<512;j+=32)
                        g.Graphics.DrawImage(img,i,j);
                context.Dispose();
            }
            g.Graphics.DrawImage(img, new Point(x, y));
        }
    }
}
