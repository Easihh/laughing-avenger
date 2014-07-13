using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Reflection;
namespace ProjectLabyrinth
{
    public partial class Form1 : Form
    {
        readonly Int16 screenWidth = 640, screenHeight = 512;
        UInt16 x, y,fps;
        Input input;
        GameAsset assets;
        Level level;
        long interval = (long)TimeSpan.FromSeconds(1.0 / 60.0).TotalMilliseconds;
        Stopwatch timer = new Stopwatch();
        Hero hero;
        Font font;
        public Form1()
        {
            hero = new Hero(50, 50);
            assets = new GameAsset();
            level = new Level();
            x = y= fps = 0;
            InitializeComponent();
            setPanelBuffering();
            input = new Input();
            drawPanel.Paint += new PaintEventHandler(Render);
            KeyPreview = true;
            KeyPress += new KeyPressEventHandler(input.KeyPressed);
            SetClientSizeCore(screenWidth, screenHeight);
            Text = "Project Labyrinth "+ "FPS:"+fps;
        }

        private void setPanelBuffering()
        {
            typeof(Panel).InvokeMember("DoubleBuffered",
             BindingFlags.SetProperty | BindingFlags.Instance | BindingFlags.NonPublic,
            null, drawPanel, new object[] { true });
        }
        private void Render(object sender, PaintEventArgs g)
        {
            SolidBrush brush = new SolidBrush(Color.White);
            font=new Font(FontFamily.GenericMonospace,18);
            level.render(g);
            hero.render(g);
            g.Graphics.DrawString("X:"+hero.x,font,brush,new PointF(512.0F,384.0F));
            g.Graphics.DrawString("Y:" + hero.x, font, brush, new PointF(512.0F, 416.0F));
        }
        private void Form1_Load(object sender, EventArgs e)
        {
        }

        public void GameLoop()
        {
            long startTime;
            Stopwatch lastFpsUpdate=new Stopwatch();
            timer.Start();
            lastFpsUpdate.Start();
            while (Created)
            {
                startTime = timer.ElapsedMilliseconds;
                fps++;
                GameLogic();
                Refresh();//call render
                Application.DoEvents();            
                while (timer.ElapsedMilliseconds - startTime < interval) ;
                if (lastFpsUpdate.ElapsedMilliseconds>= 1000)
                {
                    Text = "Project Labyrinth " + "FPS:" + fps;
                    fps = 0;
                    lastFpsUpdate.Reset();
                    lastFpsUpdate.Start();
                }
            }
        }
        private void GameLogic()
        {
            hero.update();
        }
    }
}
