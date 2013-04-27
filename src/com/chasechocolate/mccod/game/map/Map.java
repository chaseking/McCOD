package com.chasechocolate.mccod.game.map;

import org.bukkit.Location;

import com.chasechocolate.mccod.game.GameManager;
import com.chasechocolate.mccod.game.TeamColor;

public class Map {
	private String name;
	
	private Location redSpawn;
	private Location blueSpawn;
	
	public Map(GameManager gameManager){
		this.name = gameManager.getRandomMap();
		this.redSpawn = gameManager.getSpawnLocation(name, TeamColor.RED);
		this.blueSpawn = gameManager.getSpawnLocation(name, TeamColor.BLUE);
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
}