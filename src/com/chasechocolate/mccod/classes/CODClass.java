package com.chasechocolate.mccod.classes;

import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;

public abstract class CODClass {
	protected McCOD plugin;
	
	private String cmd;
	private String name;
	
	public CODClass(McCOD plugin){
		this.plugin = plugin;
	}
	
	public String getCommand(){
		return this.cmd;
	}
	
	public void setCommand(String cmd){
		this.cmd = cmd;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void apply(Player player){
		
	}
}