package de.holm.worldTimer.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import javax.imageio.ImageIO;

import org.jdom2.JDOMException;

import de.holm.worldTimer.items.Settings;
import de.holm.worldTimer.runnable.WorldUpdater;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sun.management.GcInfoCompositeData;

public class WTMainController {

	
	private Image worldBG;
	private Timer updateTimer ;
	private WTDrawer drawer;
	
	
	@FXML
	private ImageView worldImg;
	
	@FXML
	private Canvas worldCanvas;
	
	
	@FXML
	private AnchorPane anchorPane;
	
	
	
	
	public void init(String SRC_PATH, Scene mainScene){
		System.out.println("Contoller started, src path is "+SRC_PATH);

		
		try {
			
			Settings.readSettings(SRC_PATH);
			
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		//Set BG
		System.out.println("Set background color to:"+Settings.getBgColor()+" in case the map image is transparent!");
		anchorPane.setStyle("-fx-background-color: "+Settings.getBgColor()+";");
		
		File bgFile = new File(Settings.getWorldPath());
		System.out.println("loading world Image from: "+bgFile.toURI().toString());

		worldBG = new Image(bgFile.toURI().toString());
		
		//Create Drawer Object
		drawer = new WTDrawer(worldCanvas, worldBG, anchorPane);
	
		
		
		drawWorld(worldBG, worldCanvas);
		drawTimes(worldCanvas);
		
		onResizeAction(mainScene);

		
	}
	
	private void onResizeAction(Scene mainScene){
		//Set listener on resize!
		final InvalidationListener listener = new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				drawWorld(worldBG, worldCanvas);
				System.out.println("Resize to w: "+anchorPane.getWidth()+" h: "+anchorPane.getHeight());
			}
		};
		
		mainScene.widthProperty().addListener(listener);
		mainScene.heightProperty().addListener(listener);
		
	}
	
	private void drawWorld(Image img, Canvas cnvs){
	
		this.drawer.drawTimesToWorld();;

		
	}
	
	private void drawTimes(Canvas cnvs){
		WorldUpdater wupd = new WorldUpdater(drawer);
		updateTimer = new Timer();
		updateTimer.schedule(wupd, 1000, 100);
		
		
	}
	
	
	
	
}
