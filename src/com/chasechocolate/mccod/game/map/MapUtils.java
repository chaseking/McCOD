package com.chasechocolate.mccod.game.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.chasechocolate.mccod.game.TeamColor;

public class MapUtils {
	private static File mapsFile;
	
	private static FileConfiguration mapsConfig;
	
	private static List<String> mapNames = new ArrayList<String>();
	
	@SuppressWarnings("static-access")
	public MapUtils(File mapsFile, FileConfiguration mapsConfig){
		this.mapsFile = mapsFile;
		this.mapsConfig = mapsConfig;
		
		mapNames.clear();
		
		for(String map : mapsConfig.getConfigurationSection("maps").getKeys(false)){
			mapNames.add(map);
		}
	}
	
	public static boolean isMap(String map){
		boolean isMap = mapNames.contains(map);
		
		return isMap;
	}
	
	public static void createMap(String map){
		if(!(isMap(map))){
			mapsConfig.set("maps." + map, "");
			saveMapsConfig();
		}
	}
	
	public static void deleteMap(String map){
		if(isMap(map)){
			mapsConfig.set("maps." + map, null);
			saveMapsConfig();
		}
	}
	
	public static void setSpawn(String map, TeamColor team, Location loc){
		if(isMap(map)){
			String teamName = team.toString().toLowerCase();
			
			String world = loc.getWorld().getName();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			float yaw = loc.getYaw();
			float pitch = loc.getPitch();
			
			mapsConfig.set(map + "." + teamName + ".world", world);
			mapsConfig.set(map + "." + teamName + ".x", x);
			mapsConfig.set(map + "." + teamName + ".y", y);
			mapsConfig.set(map + "." + teamName + ".z", z);
			mapsConfig.set(map + "." + teamName + ".yaw", yaw);
			mapsConfig.set(map + "." + teamName + ".pitch", pitch);

			saveMapsConfig();
		}
	}
	
	public static void saveMapsConfig(){
		try{
			mapsConfig.save(mapsFile);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}