package com.chasechocolate.mccod.game.map;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;

public class Map {
	private String name;
	
	private Arena arena;
	
	private Location redSpawn;
	private Location blueSpawn;
	
	private Location point1;
	private Location point2;
	
	private File configFile;
	private FileConfiguration config;
	
	public Map(String name){
		this.name = name;
		this.redSpawn = GameUtils.getSpawnLocation(name, TeamColor.RED);
		this.blueSpawn = GameUtils.getSpawnLocation(name, TeamColor.BLUE);
	}
	
	public String getName(){
		return this.name;
	}
	
	public Location getTeamSpawn(TeamColor team){
		if(team == TeamColor.RED){
			return this.redSpawn;
		} else if(team == TeamColor.BLUE){
			return this.blueSpawn;
		} else {
			return null;
		}
	}
	
	public Arena getArena(){
		return this.arena;
	}
	
	public void setArena(Arena arena){
		this.arena = arena;
	}
	
	public Location getPoint1(){
		return this.point1;
	}
	
	public void setPoint1(Location point1){
		this.point1 = point1;
		this.config.set("region.point1.world", point1.getWorld().getName());
		this.config.set("region.point1.x", point1.getBlockX());
		this.config.set("region.point1.y", point1.getBlockY());
		this.config.set("region.point1.z", point1.getBlockZ());
		
		saveConfig();
	}
	
	public Location getPoint2(){
		return this.point2;
	}
	
	public void setPoint2(Location point2){
		this.point2 = point2;
		this.config.set("region.point2.world", point2.getWorld().getName());
		this.config.set("region.point2.x", point2.getBlockX());
		this.config.set("region.point2.y", point2.getBlockY());
		this.config.set("region.point2.z", point2.getBlockZ());
		
		saveConfig();
	}
	
	private void saveConfig(){
		try{
			config.save(configFile);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}