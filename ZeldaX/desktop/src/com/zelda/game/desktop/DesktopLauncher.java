package com.zelda.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zelda.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30=false;
		config.vSyncEnabled=true;
		config.foregroundFPS=60;
		config.height=352;
		config.width=512;
		new LwjglApplication(new Game(), config);
	}
}
