package com.logicbytez.sanctuary.desktop;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.logicbytez.sanctuary.Main;

public class DesktopLauncher{
	public static void main(String[] arg){
		boolean fullscreen = false, testing = false;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("icon.png", FileType.Internal);
		config.title = "Sanctuary";
		if(fullscreen){
			config.fullscreen = true;
			config.width = 1920;
			config.height = 1080;
		}else{
			config.resizable = false;
			config.width = 960;
			config.height = 540;
		}
		if(testing){
			config.foregroundFPS = 0;
			config.vSyncEnabled = false;
		}
		new LwjglApplication(new Main(testing, false), config);
	}
}