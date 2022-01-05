package de.holm.worldTimer.runnable;

import java.io.File;
import java.util.TimerTask;

import de.holm.worldTimer.controller.WTDrawer;
import de.holm.worldTimer.items.Settings;
import javafx.scene.image.Image;

public class GgLoader extends TimerTask  implements Runnable{

	
	
	public GgLoader(WTDrawer drawer) {
		super();
		this.drawer = drawer;
	}

	private WTDrawer drawer;
	private Image worldBG;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File bg = new File(Settings.getWorldPath());
		worldBG = new Image(bg.toURI().toString());
		
		drawer.setWtImg(worldBG);
		

	}

}
