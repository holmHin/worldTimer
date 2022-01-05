package de.holm.worldTimer.items;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Location {
	
	private String name;
	private double lat, lon;
	private int x, y;
	private clockModus mode;
	private boolean isHome;
	private String colorLable, colorCircle, colorFinish, colorCircleActive;
	private String timeZoneCode;
	private boolean active, finish;
	private Message finalCountDownTimer, finalCountDownLable; 
	
	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Location(String name) {
		super();
		this.name = name;
	}
	public String getTimeZoneCode() {
		return timeZoneCode;
	}

	public void setTimeZoneCode(String timeZoneCode) {
		this.timeZoneCode = timeZoneCode;
	}

	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(Double lat) {
		this.lat = lat;
	}


	public double getLon() {
		return lon;
	}


	public void setLon(Double lon) {
		this.lon = lon;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public clockModus getMode() {
		return mode;
	}


	public void setMode(clockModus mode) {
		this.mode = mode;
	}


	public boolean isHome() {
		return isHome;
	}


	public void setHome(boolean isHome) {
		this.isHome = isHome;
	}


	public String getColorLable() {
		return colorLable;
	}


	public void setColorLable(String colorLable) {
		this.colorLable = colorLable;
	}


	public String getColorCircle() {
		return colorCircle;
	}


	public void setColorCircle(String colorCircle) {
		this.colorCircle = colorCircle;
	}


	public String getColorFinish() {
		return colorFinish;
	}


	public void setColorFinish(String colorFinish) {
		this.colorFinish = colorFinish;
	}


	public String getColorCircleActive() {
		return colorCircleActive;
	}


	public void setColorCircleActive(String colorCircleActive) {
		this.colorCircleActive = colorCircleActive;
	}
	
	public boolean isFinish() {
		return finish;
	}
	
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	
	
	public TimeZone getTimeZone(){
		TimeZone tz;
		try {
			tz = TimeZone.getTimeZone(this.getTimeZoneCode());
		} catch (Exception e) {
			// TODO: handle exception
			tz = TimeZone.getDefault();
		}
		
		return tz;
	}
	
	public String getCurrentTime(String timeFormat){
		String ret = "";
		
		// calculate locale Time
		Date locDate = new Date();
		Calendar localeCal = new GregorianCalendar();
		localeCal.setTimeZone(this.getTimeZone());
		SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
		sf.setTimeZone(this.getTimeZone());
		ret = sf.format(locDate.getTime());

		//Check if time is active or finish
		this.calculateTimeStatus();
				
		return ret;
		
	}
	
	
public String getTimeLeft(Boolean min){
	String ret = "";
	Date locDate = new Date();
	
	//Calculate end Time
	Calendar midNigth = Calendar.getInstance();
	midNigth.set(Calendar.HOUR_OF_DAY, 0);
	midNigth.set(Calendar.MINUTE, 0);
	midNigth.set(Calendar.SECOND, 0);
	midNigth.set(Calendar.MILLISECOND, 0);

	// calculate TimeZone Offset and minutes to endtime
	Date midTime = Settings.getTimerDate();
	
	Long timeToEnd = ((locDate.getTime() + this.getTimeZone().getOffset(locDate.getTime()) - midTime.getTime()) );
	timeToEnd = timeToEnd - (Settings.getLocalTimeZone().getOffset(locDate.getTime()) );
	
	if(timeToEnd < 0){
		timeToEnd = timeToEnd * -1;
	}
	
	
	final TimeUnit convert = TimeUnit.MILLISECONDS;
	long daysLeft =  convert.toDays(timeToEnd);
	timeToEnd -= DAYS.toMillis(daysLeft);
	long hoursLeft = convert.toHours(timeToEnd);
	timeToEnd -= HOURS.toMillis(hoursLeft);
	long minsLeft = convert.toMinutes(timeToEnd);
	timeToEnd -= MINUTES.toMillis(minsLeft);
	long secondsLeft = convert.toSeconds(timeToEnd);
	timeToEnd -= SECONDS.toMillis(timeToEnd);
	
	
//	System.out.println(loc.getName()+" time "+locDate.getTime()+ "/ finish time: "+Settings.getTimerDate().getTime());
// System.out.println(loc.getName()+ "offset "+tz.getOffset(Settings.getTimerDate().getTime()));
	
	
	if(daysLeft>1)
		ret = String.format("%02d Days %02d:%02d:%02d",daysLeft, hoursLeft, minsLeft, secondsLeft);
	else if(daysLeft == 1)
		ret = String.format("%02d Day %02d:%02d:%02d",daysLeft, hoursLeft, minsLeft, secondsLeft);
	else if(min)	
		ret = String.format("%02d:%02d", minsLeft, secondsLeft);
	else	
		ret = String.format("%02d:%02d:%02d", hoursLeft, minsLeft, secondsLeft);

	//Calculate if time is finished or activated
	this.calculateTimeStatus();
	
	return ret;
	
}
	
private void calculateTimeStatus(){
	// Calculate Timedifference to Midnight if finish is midnight!
	Date locDate = new Date();
	
	Calendar midNigth = Calendar.getInstance();
	midNigth.set(Calendar.HOUR_OF_DAY, 0);
	midNigth.set(Calendar.MINUTE, 0);
	midNigth.set(Calendar.SECOND, 0);
	midNigth.set(Calendar.MILLISECOND, 0);

	// calculate TimeZone Offset and minutes to midnight
	Date midTime = Settings.getTimerDate();
	Long difference = ((locDate.getTime() + this.getTimeZone().getOffset(locDate.getTime()) - midTime.getTime()) / 1000) / 60;
	difference = difference - (Settings.getLocalTimeZone().getOffset(locDate.getTime()) / 1000 / 60);
	
	
	
	
	// Check if Location need to be activated!
	int minsPriorActive = Settings.getActiveStart() * (-1);
	if (difference > minsPriorActive && difference < Settings.getActiveEnd()) {
		
		if(this.isActive()==false){
			//Message that location is activated!
			Settings.printMsg(this.getName()+" is next!", this.getColorLable(), 14, 2);
			System.out.println(this.getName()+" was activated!");
		}
		this.setActive(true);
	} else
		this.setActive(false);

	
	if(difference>(-11) && this.finalCountDownTimer == null ){
		this.finalCountDownLable = new Message(this.name+" in:", this.getColorLable(), 14, 1);
		this.finalCountDownTimer = new Message("10:00 ", this.getColorLable(), 28, 1);
		Settings.getMessageBox().add(finalCountDownLable);
		Settings.getMessageBox().add(finalCountDownTimer);
	}
	
	
	// Check if Location is Finished!
	int minsPriorFinish = Settings.getFinishStart() * (-1);
	if (difference >= minsPriorFinish && difference <= Settings.getFinishEnd() ) {
		if(this.isFinish()==false){
			//Message that this location will be set to finished
			System.out.println(this.getName()+" reached the due date!");
			Settings.printMsg(this.getName()+" is due!", this.getColorFinish(), 14, 2);
			if(this.finalCountDownTimer != null)
				this.finalCountDownTimer.setPriority(-1);
			if(this.finalCountDownLable != null){
				this.finalCountDownLable.setPriority(-1);
			}
			
		}
		this.setFinish(true);
	} else
		this.setFinish(false);

	
}

public void calculateFinalCountDown(){
	if(this.finalCountDownTimer != null){
		this.finalCountDownTimer.setMessage(this.getTimeLeft(true));
	}
}
	
	
	

}
