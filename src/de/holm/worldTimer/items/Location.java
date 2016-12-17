package de.holm.worldTimer.items;

public class Location {
	
	private String name;
	private double lat, lon;
	private int x, y;
	private clockModus mode;
	private boolean isHome;
	private String colorLable, colorCircle, colorFinish, colorCircleActive;
	private String timeZoneCode;
	private boolean active, finish;
	
	
	
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
	
	
	
	
	

}
