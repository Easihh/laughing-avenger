using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
namespace ProjectLabyrinth
{
    public class Input
    {
        public Input(){}
        internal void KeyPressed(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar==32)//space
                System.Diagnostics.Debug.WriteLine("Space was pressed");
            if (e.KeyChar == 119)//W
                System.Diagnostics.Debug.WriteLine("W was pressed");
            if (e.KeyChar == 97)//A
                System.Diagnostics.Debug.WriteLine("A was pressed");
            if (e.KeyChar == 115)//S
                System.Diagnostics.Debug.WriteLine("S was pressed");
            if (e.KeyChar == 100)//D
                System.Diagnostics.Debug.WriteLine("D was pressed");
        }
    }
}
