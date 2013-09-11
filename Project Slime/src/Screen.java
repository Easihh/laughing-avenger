import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;


public class Screen {
	GraphicsDevice vc;
	
	public Screen(){
		GraphicsEnvironment env=GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc=env.getDefaultScreenDevice();
	}
	public void setFullScreen(DisplayMode dm,JFrame window){
		window.setUndecorated(true);
		window.setResizable(false);
		if(vc.isFullScreenSupported())
			vc.setFullScreenWindow(window);
		if(dm!=null && vc.isDisplayChangeSupported())
			vc.setDisplayMode(dm);
	}
}
