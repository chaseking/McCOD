package com.chasechocolate.mccod.game.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.utils.Config;

public class MapUtils {
	private static File baseMapsFile;
	
	private static List<String> mapNames = new ArrayList<String>();
	
	public static HashMap<String, File> mapFiles = new HashMap<String, File>();
	public static HashMap<String, FileConfiguration> mapConfigs = new HashMap<String, FileConfiguration>();
	
	@SuppressWarnings("static-access")
	public MapUtils(){
		this.baseMapsFile = Config.getBaseMapsFile();;
		
		mapNames.clear();
		
		for(File mapFile : baseMapsFile.listFiles()){
			String mapName = mapFile.getName().replaceAll(".yml", "");
			
			mapNames.add(mapName);
			mapFiles.put(mapName, mapFile);
		}
	}
	
	public static File getMapFile(String mapName){
		if(isMap(mapName)){
			File mapFile = mapFiles.get(mapName);
			return mapFile;
		} else {
			return null;
		}
	}
	
	public static FileConfiguration getMapConfig(String mapName){
		if(isMap(mapName)){
			FileConfiguration mapConfig = mapConfigs.get(mapName);
			return mapConfig;
		} else {
			return null;
		}
	}
	
	public static boolean isMap(String map){
		boolean isMap = mapNames.contains(map);
		
		return isMap;
	}
	
	public static void createMap(String map){
		if(!(isMap(map))){
			getMapConfig(map).set("maps." + map, "");
			saveMapsConfig(map);
		}
	}
	
	public static void deleteMap(String mapName){
		if(isMap(mapName)){
			if(getMapFile(mapName) != null){
				getMapFile(mapName).delete();
			}
		}
	}
	
	public static void setSpawn(String mapName, TeamColor team, Location loc){
		if(isMap(mapName)){
			if(getMapConfig(mapName) != null){
				FileConfiguration mapsConfig = getMapConfig(mapName);
				String teamName = team.toString().toLowerCase();
				
				String world = loc.getWorld().getName();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				float yaw = loc.getYaw();
				float pitch = loc.getPitch();
				
				mapsConfig.set(mapName + "." + teamName + ".world", world);
				mapsConfig.set(mapName + "." + teamName + ".x", x);
				mapsConfig.set(mapName + "." + teamName + ".y", y);
				mapsConfig.set(mapName + "." + teamName + ".z", z);
				mapsConfig.set(mapName + "." + teamName + ".yaw", yaw);
				mapsConfig.set(mapName + "." + teamName + ".pitch", pitch);
				
				saveMapsConfig(mapName);
			}
		}
	}
	
	
	
	public static void saveMapsConfig(String mapName){
		try{
			YamlConfiguration.loadConfiguration(mapFiles.get(mapName)).save(mapFiles.get(mapName));
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}