package de.holm.worldTimer.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.holm.worldTimer.items.Location;
import de.holm.worldTimer.items.Message;
import de.holm.worldTimer.items.Settings;
import de.holm.worldTimer.items.clockModus;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static java.util.concurrent.TimeUnit.*;

/**
 * @author holm
 *
 */

/**
 * @author holm
 *
 */
/**
 * @author holm
 *
 */
public class WTDrawer {

	private Canvas wtCanvas;
	private Image wtImg;
	private AnchorPane aPane;
	private double equator = 0;
	private double northpole = 0;
	private double southpole = 0;
	private double medirian = 0;
	private double angle = 0;
	private int zoomer = 0;
	private double alphaFinish = 1.0;
	private boolean alphaFinishDirection = true;
	private int glow = 50;

	// Getters Setters
	public AnchorPane getaPane() {
		return aPane;
	}

	public void setaPane(AnchorPane aPane) {
		this.aPane = aPane;
	}

	public Canvas getWtCanvas() {
		return wtCanvas;
	}

	public void setWtCanvas(Canvas wtCanwas) {
		this.wtCanvas = wtCanwas;
	}

	public Image getWtImg() {
		return wtImg;
	}

	public void setWtImg(Image wtImg) {
		this.wtImg = wtImg;
	}

	public WTDrawer(Canvas wtVanwas, Image wtImg, AnchorPane aPane) {
		super();
		this.wtCanvas = wtVanwas;
		this.wtImg = wtImg;
		this.aPane = aPane;
		calculatGridPoints();
	}

	public void drawWorld() {
		wtCanvas.setWidth(aPane.getWidth());
		wtCanvas.setHeight(aPane.getHeight());
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();

		// Draw image to fit!
		gc.drawImage(this.wtImg, 0, 0, this.aPane.getWidth(), this.aPane.getHeight());

	}

	public void drawTimesToWorld() {
		this.drawWorld();
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();

		glow = Settings.getGlowEffect();
		
		// LableColor
		Color c = Color.web(Settings.getLableColor(), 0.9);
		gc.setFill(c);

		// Draw current Time
		if(glow>0)
		gc.setEffect(new Glow(glow));
		Font theFont = Font.font("Courier New", FontWeight.BOLD, 22);
		gc.setFont(theFont);
		double timeLeft = (aPane.getWidth() > 200 ? aPane.getWidth() - 150 : 1);
		gc.fillText(getCurrentTimeAsString(), timeLeft, 30);

		// Draw Title
		theFont = Font.font("Arial", FontWeight.BOLD, 25);
		gc.setFont(theFont);  
		gc.fillText(Settings.getTitle(), 25, 31);
		gc.setEffect(null);

		// draw locations
		for (Location myLoc : Settings.getLocationList()) {
			this.drawCity(myLoc);
		}
		
		
		//Draw infoBox
		if(Settings.isShowBox())
			this.drawInfoBox();

		// Animation calculation
		this.calcZoomer();
		this.calcFinishAlpha();

	}

	/**
	 * This function calculates the alpha channel pulsing for a Finish location!
	 */
	private void calcFinishAlpha() {
		if (alphaFinishDirection) {
			if (alphaFinish > 0.2) {
				alphaFinish = alphaFinish - 0.05;
			} else
				alphaFinishDirection = false;
		} else {
			if (alphaFinish < 1.0) {
				alphaFinish = alphaFinish + 0.05;
			} else
				alphaFinishDirection = true;
		}
	}

	/**
	 * This Function calculates the multiplier for the pulsing circle if a
	 * location is set to active!
	 * 
	 */

	private void calcZoomer() {
		if (this.zoomer > 0) {
			this.zoomer = this.zoomer - 1;
		} else
			this.zoomer = 7;
	}

	private String getCurrentTimeAsString() {
		Date now = new Date();
		SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");

		return sp.format(now);

	}

	private String getTimerDateAsString() {
		SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return sf.format(Settings.getTimerDate());
	}

	/*
	 * private void drawGreenwich(){ GraphicsContext gc =
	 * wtCanvas.getGraphicsContext2D(); gc.setStroke(Color.RED);
	 * gc.strokeOval(this.updatePositionXtowindow(Settings.getxWBoundGreenwitch(
	 * )), this.updatePositionYtowindow(Settings.getyWBoundGreenwitch()), 5, 5);
	 * Font theFont = Font.font( "Courier New", FontWeight.BOLD, 10 );
	 * gc.setFont( theFont ); Color c = Color.web(Settings.getLableColor(),0.9);
	 * gc.setFill(c); gc.fillText( "Greenwitch: "+getTimerDateAsString(),
	 * this.updatePositionXtowindow(Settings.getxWBoundGreenwitch())+6,
	 * this.updatePositionYtowindow(Settings.getyWBoundGreenwitch()) ); }
	 */
	/**
	 * @param i
	 * @return Function returns the updated x position to current window
	 *         resolution
	 */
	private double updatePositionXtowindow(int i) {
		Double x = i / wtImg.getWidth() * aPane.getWidth();

		return x;
	}

	/**
	 * @param i
	 * @return Function returns the updated y position to current window
	 *         resolution
	 */
	private double updatePositionYtowindow(int i) {
		Double y = i / wtImg.getHeight() * aPane.getHeight();
		return y;
	}

	/**
	 * @param lat
	 * @return Function returns the pixel x position by the given latitude
	 * 
	 */
	private double getXfromLongitude(Double lon) {
		double x = 0;
		x = this.medirian + ((this.medirian - this.angle) * ((lon / 18000)));
		return x;
	}

	/**
	 * @param lon
	 * @return Function returns the pixel y position by the given longitude
	 * 
	 */
	private double getYFromLatitude(Double lan) {

		double x = 0;
		x = this.equator + (this.equator - this.northpole) * ((lan / 9000) * -1);
		return x;
	}

	/**
	 * This Function will calculate the Grid Points by the given reference
	 * points The result will be stored into the class variables for world
	 * bounds
	 * 
	 */
	private void calculatGridPoints() {

		final double eqDistMulti = 0.5850858247; // Multiplier to longitude
													// distance between london
													// and auckland##
		final double npDistMulti = 1.7485914125; // Multiplier to longitude
													// distance between equator
													// and london##
		final double agDistMulti = 1.1565893465; // Multiplier to latitude
													// distance london hawai of
													// angle 180°

		this.equator = Settings.getyWBoundGreenwitch()
				+ (eqDistMulti * (Settings.getyWBoundAuckland() - Settings.getyWBoundGreenwitch()));
		this.northpole = (this.equator - (this.equator - Settings.getyWBoundGreenwitch()) * npDistMulti);
		this.southpole = (this.equator + (Settings.getyWBoundAuckland() - this.equator) * npDistMulti);
		this.angle = Settings.getxWBoundGreenwitch()
				- ((Settings.getxWBoundGreenwitch() - Settings.getxWBoundHawai()) * agDistMulti);
		this.medirian = Settings.getxWBoundGreenwitch();
		System.out
				.println("Map Bounds are: Northpole: " + this.northpole + "; Southpole: " + this.southpole + "; Angle: " + this.angle);
		double southpole = 0;
	}

	@Deprecated
	private void drawCity(String name, Double lat, Double lon) {

		System.out.println("City: " + name + " X: " + (int) this.getXfromLongitude(lat) + " Y: "
				+ (int) this.getYFromLatitude(lon));

		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		gc.setStroke(Color.RED);
		gc.strokeOval(this.updatePositionXtowindow((int) getXfromLongitude(lat)),
				this.updatePositionYtowindow((int) this.getYFromLatitude(lon)), 5, 5);
		Font theFont = Font.font("Courier New", FontWeight.BOLD, 10);
		gc.setFont(theFont);
		Color c = Color.web(Settings.getLableColor(), 0.9);
		gc.setFill(c);
		gc.fillText(name, this.updatePositionXtowindow((int) getXfromLongitude(lat)) + 6,
				this.updatePositionYtowindow((int) this.getYFromLatitude(lon)));

	}

	private void drawCity(Location loc) {

		if (loc.isActive())
			this.drawActiveCircle(loc);
		else
			this.drawCircle(loc);
		this.drawLocLable(loc);
		this.drawLocaleTime(loc);

	}

	private void drawCircle(Location loc) {
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		Color circleColor;
		try {
			circleColor = Color.web(loc.getColorCircle(), 0.9);
		} catch (Exception e) {
			circleColor = Color.RED;
			// TODO: handle exception
		}
		
		if(glow>0)
			gc.setEffect(new Glow(glow));
		gc.setStroke(circleColor);
		gc.strokeOval(this.updatePositionXtowindow((int) getXfromLongitude(loc.getLon())),
				this.updatePositionYtowindow((int) this.getYFromLatitude(loc.getLat())), 5, 5);
		gc.setEffect(null);

	}

	/**
	 * @param loc
	 *            Draw a pulsing circle to the map
	 */
	private void drawActiveCircle(Location loc) {
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		Color circleColor;
		try {
			circleColor = Color.web(loc.getColorCircleActive(), 0.9);
		} catch (Exception e) {
			circleColor = Color.RED;
			// TODO: handle exception
		}

		// Pulsing circle
		gc.setEffect(new Glow(50));
		gc.setStroke(circleColor);
		int posCorrection = (5 * this.zoomer) / 2;
		gc.strokeOval(this.updatePositionXtowindow((int) getXfromLongitude(loc.getLon())) - posCorrection,
				this.updatePositionYtowindow((int) this.getYFromLatitude(loc.getLat())) - posCorrection,
				5 * this.zoomer, 5 * this.zoomer);
		gc.setEffect(null);
	}

	/**
	 * @param loc
	 *            Draw the name of location to the map
	 */
	private void drawLocLable(Location loc) {
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		Color lableColor;
		try {
			if (loc.isFinish()) {
				lableColor = Color.web(Settings.getFinishColor(), this.alphaFinish);
			} else
				lableColor = Color.web(loc.getColorLable(), 0.95);
		} catch (Exception e) {
			lableColor = Color.RED;
			// TODO: handle exception
		}

		int fSize = (loc.isHome() ? 13 : 13);
		Font lableFont = Font.font("Arial", FontWeight.BOLD, fSize);
		if(glow>0)
			gc.setEffect(new Glow(glow));
		gc.setFont(lableFont);
		gc.setFill(lableColor);
		gc.fillText(loc.getName(), this.updatePositionXtowindow((int) getXfromLongitude(loc.getLon())) + 6,
				this.updatePositionYtowindow((int) this.getYFromLatitude(loc.getLat())) - 3);
		gc.setEffect(null);

	}

	/**
	 * @param loc
	 * 
	 *            This function will draw the locale Time to the map depending
	 *            to the mode! It will also calculate the time offset depending
	 *            on the Timezone which is assigned to the location! And will
	 *            set an location as active or finished
	 */

	private void drawLocaleTime(Location loc) {
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();

		// Get Color
		Color lableColor;
		try {
			if (loc.isFinish()) {
				lableColor = Color.web(loc.getColorFinish(), this.alphaFinish);
			} else
				lableColor = Color.web(loc.getColorLable(), 0.95);
		} catch (Exception e) {
			lableColor = Color.RED;
			// TODO: handle exception

		}

		
		Font lableFont = Font.font("Arial", FontWeight.BOLD, 15);
		
		String timeDrawer = "";

		//Mode
		if(loc.getMode()==clockModus.Time){
			timeDrawer = loc.getCurrentTime("HH:mm:ss");
		} else if(loc.getMode()==clockModus.Countdown){
			timeDrawer = loc.getTimeLeft(false);
		}
		
		//Calculate final CountDown
		loc.calculateFinalCountDown();
		
		
		// Finally Draw Time
		if(glow>0)
			gc.setEffect(new Glow(glow));
		gc.setFont(lableFont);
		gc.setFill(lableColor);
		gc.fillText(timeDrawer, this.updatePositionXtowindow((int) getXfromLongitude(loc.getLon())) + 6,
				this.updatePositionYtowindow((int) this.getYFromLatitude(loc.getLat())) + 10);
		gc.setEffect(null);

	}
	
	
	private void drawInfoBox(){
		
		
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		// LableColor
		
		double fx, fy, fHeight, fWidth;
		fx = (this.aPane.getWidth()/30);
		fy = ((aPane.getHeight()/2)-(aPane.getHeight()/15));
		fHeight = (this.aPane.getWidth()/5);
		fWidth = this.aPane.getHeight()/2;
		
		if(glow>0)
			gc.setEffect(new Glow(glow));
		Color cF = Color.web(Settings.getLableColor(), 0.9);
		Color cB = Color.web(Settings.getLableColor(), 0.1);
		gc.setFill(cB);
		gc.setStroke(cF);
		gc.strokeRect(fx, fy ,fHeight , fWidth);
		gc.fillRect(fx, fy ,fHeight , fWidth);
		gc.setEffect(null);
		
		drawInfoLines(fHeight, fWidth, fx, fy);
		
	}
	
	private void drawInfoLines(double width, double height, double posX, double posY){
		GraphicsContext gc = wtCanvas.getGraphicsContext2D();
		
		
		double pos = posY;
		
		//remove deactivated messages (priority < 0)
		for (int i = 0; i < Settings.getMessageBox().size(); i++) {
			if(Settings.getMessageBox().get(i).getPriority()<0){
				Settings.getMessageBox().remove(i);
			}
		}
		
		//Load msg lines:
				for(Message msg: Settings.getMessageBox()){
					String msgText = msg.getMessage();
					double textWidth = (msg.getFontSize()*msgText.length())*0.60;
					while((textWidth+10)>width){
						msgText = msgText.substring(0, msgText.length()-1);
						textWidth = (msg.getFontSize()*msgText.length())*0.60;
						
					}
					
					pos=pos+(1.5*msg.getFontSize());
					Color cF = Color.web(msg.getColor(), 0.9);
					gc.setFill(cF);
					Font theFont = Font.font("Lucida Sans Unicode", FontWeight.NORMAL, msg.getFontSize());
					gc.setFont(theFont);
					gc.fillText(msgText, (posX+10), pos);
											//System.out.println(width+" .... "+msg.getMessage().length()+"....."+textWidth);
				}
			if((pos-posY+10)>height){
				Settings.getMessageBox().remove(0);
			}

	}

}
