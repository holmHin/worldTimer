package de.holm.worldTimer.items;

public class Message {

	
	
	public Message(String message, String color, int fontSize, int priority) {
		super();
		this.message = message;
		this.color = color;
		this.fontSize = fontSize;
		this.priority = priority;
	}
	
	
	String message, color;
	int fontSize, priority;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	
	
	
}
