package com.example.scroller;

import android.app.Application;

public class ScrollerApplication extends Application {
	
	//defaults for when the app starts up
	private int scrollSpeed = 3, textColor = 0xFF33B5E5, bgColor = 0xFF000000;
	private String text = "Hello world!";
	private boolean autoScroll = true;
	
	//acessor and mutator methods for the settings so activities can share settings
	public void setText(String newText){
		text = newText;
	}
	public String getText(){
		return text;
	}
	
	public void setSpeed(int newSpeed){
		scrollSpeed = newSpeed;
	}
	public int getSpeed(){
		return scrollSpeed;
	}
	
	public void setTextColor(int color){
		textColor = color;
	}
	public int getTextColor(){
		return textColor;
	}
	
	public void setBgColor(int color){
		bgColor = color;
	}
	public int getBgColor(){
		return bgColor;
	}
	
	public void setAutoScroll(boolean auto){
		autoScroll = auto;
	}
	public boolean getAutoScroll(){
		return autoScroll;
	}
}
