package com.chasechocolate.mccod.game;

public enum TeamColor {
	RED,
	BLUE;
	
	public String toString(){
		String text;
		
		switch(this){
		case RED:
			text = "red";
		case BLUE:
			text = "blue";
		default:
			text = "NULL";
		}
		
		return text;
	}
}