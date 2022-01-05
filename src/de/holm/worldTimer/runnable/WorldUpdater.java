package de.holm.worldTimer.runnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import de.holm.worldTimer.controller.WTDrawer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WorldUpdater extends TimerTask implements  Runnable {
	
	private WTDrawer wtDrawer;
	

	public WorldUpdater(WTDrawer wtDrawer) {
		super();
		this.wtDrawer = wtDrawer;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wtDrawer.drawTimesToWorld();
				
				
			}
		});
		
		

	}

	

}
