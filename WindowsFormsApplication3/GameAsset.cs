using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectLabyrinth
{
    class GameAsset
    {
        static public List<Image> Game_Tileset = new List<Image>();
        readonly int tileSize = 32;
        public GameAsset()
        {
           loadTilesetImage();
        }

        private void loadTilesetImage()
        {
            Bitmap img = Properties.Resources.game_tileset;
            int rows = img.Height / tileSize;
            int cols = img.Width / tileSize;
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {              
                    Image anImage = img.Clone(new Rectangle(j * tileSize, i * tileSize, tileSize, tileSize), img.PixelFormat);
                    Game_Tileset.Add(anImage);
                }
            }
        }
    }
}
