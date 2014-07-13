using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
namespace ProjectLabyrinth
{
    class Hero
    {
        public int x, y;
        public Hero(int x,int y)
        {
            this.x = x;
            this.y = y;
        }
        public void update()
        {
        }

        public void render(PaintEventArgs g)
        {
            //g.Graphics.DrawImage(GameAsset.Game_Tileset.ElementAt(0), new Point(x, y));
        }
    }
}
