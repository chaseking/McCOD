package com.chasechocolate.mccod.game;

import org.bukkit.ChatColor;

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
	
	public ChatColor toChatColor(){
		ChatColor color;
		
		switch(this){
		case RED:
			color = ChatColor.RED;
		case BLUE:
			color = ChatColor.BLUE;
		default:
			color = ChatColor.WHITE;
		}
		
		return color;
	}
}