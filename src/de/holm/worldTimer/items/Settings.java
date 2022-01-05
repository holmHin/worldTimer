package de.holm.worldTimer.items;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;

public class Settings {

private static String settingsPath;	
private static String title;	

private static Date timerDate;
	
private static String worldPath;
private static int xWBoundGreenwitch;
private static int yWBoundGreenwitch;	
private static int xWBoundHawai;
private static int yWBoundHawai;	
private static int xWBoundAuckland;
private static int yWBoundAuckland;

private static int glowEffect;
private static boolean showBox;

private static int activeStart, activeEnd, finishStart, finishEnd;

//ColorCodes
private static String bgColor;
private static String lableColor;
private static String finishColor;

//locations
private static Vector<Location> locationList;

//MessageLines
private static Vector<Message> messageBox;

public static void readSettings(String path) throws JDOMException, IOException{
	
	Settings.settingsPath = path;
	Document doc = new SAXBuilder().build(path); 
 	Element root = doc.getRootElement();
 	//'BG Path'
 	Settings.worldPath = root.getChildText("worldPath");
 	
 	//BGColor
 	Settings.title = root.getChildText("title");
 	
 	//BGColor
 	Settings.bgColor = root.getChildText("BGColor");
 	
 	//LableColor
 	Settings.lableColor = root.getChildText("LableColor");
 	
 	//BGColor
 	Settings.finishColor = root.getChildText("FinishColor");

	//Bounds
 	try {
 		Element bounds =root.getChild("worldBounds");
 		Settings.xWBoundGreenwitch = bounds.getChild("greenwich").getAttribute("x").getIntValue();
 		Settings.yWBoundGreenwitch = bounds.getChild("greenwich").getAttribute("y").getIntValue();
 		Settings.xWBoundHawai = bounds.getChild("hawai").getAttribute("x").getIntValue();
 		Settings.yWBoundHawai = bounds.getChild("hawai").getAttribute("y").getIntValue();
 		Settings.xWBoundAuckland = bounds.getChild("auckland").getAttribute("x").getIntValue();
 		Settings.yWBoundAuckland = bounds.getChild("auckland").getAttribute("y").getIntValue();
	
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("Error by loading map bounds, this can cause problems by displaying locations. Please check XML!");
		System.out.println(e.getMessage());
	}
 	
 	//getTimerDate
 	String dateTo = root.getChildText("dateTo");
 	String timeTo = root.getChildText("timeTo");
 	
 	//getActiveTime
 	try {
 		activeStart = Integer.parseInt(root.getChild("minutesOfActive").getAttributeValue("from"));
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		System.out.println("Could't read Active From, will be set to 5 Mins prior!");
		activeStart = 5;
	}
 	
 	try {
		activeEnd = Integer.parseInt(root.getChild("minutesOfActive").getAttributeValue("to"));
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		System.out.println("Could't read Active To, will be set to 0 Mins post!");
		activeStart = 0;
	}
 	
 	try {
 		finishStart = Integer.parseInt(root.getChild("minutesOfFinish").getAttributeValue("from"));
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		System.out.println("Could't read Finish From, will be set to 0 Mins prior!");
		finishStart = 0;
	}
 	
 	try {
		finishEnd = Integer.parseInt(root.getChild("minutesOfFinish").getAttributeValue("to"));
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		System.out.println("Could't read Finish To, will be set to 1 Min post!");
		finishEnd = 1;
	}
 	
 	
 	try {
		Settings.timerDate = Settings.readDateFromString(dateTo, timeTo);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		System.out.println("Couldn't read time from XML File!");
		e.printStackTrace();
	}
 	
 	// Get Glow effect
 	try {
 		glowEffect = Integer.parseInt(root.getChild("effect").getAttributeValue("lableGlow"));
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("I was not able to load the glow effect, please check XML");
		glowEffect = 0;
	}
 	
 	// Show MsgBox
 	try {
		Settings.showBox = (root.getChild("infoBox").getAttributeValue("visible").toUpperCase().equals("TRUE")?true:false);
	} catch (Exception e) {
		// TODO: handle exception
	}
 	
 	locationList = new Vector<>();
 	
 	//Initilize MessageBox
 	messageBox = new Vector<Message>();
 	
 	// Get Locations
 	for (Element location : root.getChild("locations").getChildren()) {
		try {
	 		System.out.println("Loading location "+location.getAttributeValue("name")+" having timezone: "+location.getChild("time").getAttributeValue("zone"));
			Location loc = new Location(location.getAttributeValue("name"));
			
			// Get Location //Error Handling need to be set up!
			loc.setLat(Double.parseDouble(location.getChild("position").getAttributeValue("lat")));
			loc.setLon(Double.parseDouble(location.getChild("position").getAttributeValue("long")));
			
			//setMode
			String modestring = location.getChild("mode").getAttributeValue("timeMode");
			String isHome = location.getChild("mode").getAttributeValue("timeMode");
			switch (modestring) {
			case "0":
				loc.setMode(clockModus.Countdown);
				break;
			case "1":
				loc.setMode(clockModus.Time);
				break;

			default:
				loc.setMode(clockModus.None);
				break;
			}
			
			loc.setHome((isHome.equals("1")?true:false));
			
			//Set ColoCodes
			loc.setColorLable(location.getChild("color").getChild("lable").getAttributeValue("hex"));
			loc.setColorCircle(location.getChild("color").getChild("circle").getAttributeValue("hex"));
			loc.setColorFinish(location.getChild("color").getChild("finish").getAttributeValue("hex"));
			loc.setColorCircleActive(location.getChild("color").getChild("active").getAttributeValue("hex"));
			
			loc.setTimeZoneCode(location.getChild("time").getAttributeValue("zone"));
			
			locationList.add(loc);
			Settings.printMsg("Location "+loc.getName()+", "+loc.getTimeZoneCode()+ " loaded!", Settings.getLableColor(), 10, 2);	
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		
	}
 	
}


private static Date readDateFromString(String dateTo, String timeTo) throws ParseException{
	Date retDate = new Date();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	retDate = sf.parse(dateTo+" "+timeTo);
	
	return retDate;
}

public static TimeZone getLocalTimeZone(){
	Calendar now = Calendar.getInstance();
	TimeZone tzDefault = now.getTimeZone();
	
	return tzDefault;
}


public static String getBgColor() {
	return bgColor;
}


public static Vector<Location> getLocationList() {
	return locationList;
}

public static String getWorldPath() {
	return worldPath;
}
public static int getxWBoundGreenwitch() {
	return xWBoundGreenwitch;
}
public static int getyWBoundGreenwitch() {
	return yWBoundGreenwitch;
}
public static int getxWBoundHawai() {
	return xWBoundHawai;
}
public static int getyWBoundHawai() {
	return yWBoundHawai;
}
public static int getxWBoundAuckland() {
	return xWBoundAuckland;
}
public static int getyWBoundAuckland() {
	return yWBoundAuckland;
}
public static String getLableColor() {
	return lableColor;
}
public static String getFinishColor() {
	return finishColor;
}
public static String getTitle() {
	return title;
}
public static Date getTimerDate() {
	return timerDate;
}
public static int getActiveStart() {
	return activeStart;
}
public static int getActiveEnd() {
	return activeEnd;
}
public static int getFinishStart() {
	return finishStart;
}
public static int getFinishEnd() {
	return finishEnd;
}

public static int getGlowEffect() {
	return glowEffect;
}

public static boolean isShowBox() {
	return showBox;
}


public static Vector<Message> getMessageBox() {
	return messageBox;
}

public static void printMsg(String msg, String color, int size, int priority){
	Message newMsg = new Message(msg, color, size, priority);
	Settings.getMessageBox().add(newMsg);
}





}
