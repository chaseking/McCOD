package com.chasechocolate.mccod.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.chasechocolate.mccod.McCOD;

public class Config {
	private static McCOD plugin;
	
	private static File dataFolder;
	
	private static File configFile;
	private static File locationsFile;
	private static File gunsFile;
	private static File arenasFile;
	
	private static File baseMapsFile;
	
	private static FileConfiguration config;
	private static FileConfiguration locationsConfig;
	private static FileConfiguration gunsConfig;
	private static FileConfiguration arenasConfig;
	
	@SuppressWarnings("static-access")
	public Config(McCOD plugin){
		this.plugin = plugin;
		
		this.dataFolder = plugin.getDataFolder();
		
		this.configFile = new File(plugin.getDataFolder(), "config.yml");
		this.locationsFile = new File(plugin.getDataFolder(), "locations.yml");
		this.gunsFile = new File(plugin.getDataFolder(), "guns.yml");
		this.arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
		
		this.baseMapsFile = new File(plugin.getDataFolder() + File.separator + "maps");
		
		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.locationsConfig = YamlConfiguration.loadConfiguration(this.locationsFile);
		this.gunsConfig = YamlConfiguration.loadConfiguration(this.gunsFile);
		this.arenasConfig = YamlConfiguration.loadConfiguration(this.arenasFile);
	}
	
	//CREATING FILES
	public static void createAllFiles(){
		if(!(configFile.exists())){
			plugin.log("Found no config.yml! Creating one for you...");
			plugin.saveResource("config.yml", true);
			plugin.log("Successfully created config.yml!");
		}
		
		if(!(locationsFile.exists())){
			plugin.log("Found no locations.yml! Creating one for you...");
			plugin.saveResource("locations.yml", true);
			plugin.log("Successfully created locations.yml!");
		}
		
		if(!(gunsFile.exists())){
			plugin.log("Found no guns.yml! Creating one for you...");
			plugin.saveResource("guns.yml", true);
			plugin.log("Successfully created guns.yml!");
		}
		
		if(!(arenasFile.exists())){
			plugin.log("Found no arenas.yml! Creating one for you...");
			plugin.saveResource("arenas.yml", true);
			plugin.log("Successfully created arenas.yml!");
		}
		
		if(!(baseMapsFile.exists())){
			plugin.log("Found no maps folder! Creating one for you...");
			
			try{
				baseMapsFile.createNewFile();
				plugin.log("Successfully created arenas.yml!");
			} catch(IOException e){
				e.printStackTrace();
				plugin.log("Failed to create the maps folder!");
			}
		}
	}
	
	public static FileConfiguration createMapFile(String mapName){
		File mapFile = new File(dataFolder + File.separator + "maps", mapName + ".yml");
		FileConfiguration mapConfig = YamlConfiguration.loadConfiguration(mapFile);
		
		if(!(mapFile.exists())){
			try{
				mapFile.createNewFile();
				mapConfig.load(mapFile);
			} catch(IOException e){
				e.printStackTrace();
			} catch(InvalidConfigurationException e){
				e.printStackTrace();
			}
		}
		
		return mapConfig;
	}
	
	//GETTING FILES
	public static File getConfigFile(){
		return configFile;
	}
	
	public static File getLocationsFile(){
		return locationsFile;
	}
	
	public static File getGunsFile(){
		return gunsFile;
	}
	
	public static File getArenasFile(){
		return configFile;
	}
	
	public static File getMapsFile(String mapName){
		File mapFile = new File(dataFolder + File.separator + "maps", mapName + ".yml");
		
		if(mapFile.exists()){
			return mapFile;
		} else {
			return null;
		}
	}
	
	public static File getBaseMapsFile(){
		return baseMapsFile;
	}
	
	//ACCESSING FILECONFIGURATIONS
	public static FileConfiguration getConfig(){
		return config;
	}
	
	public static FileConfiguration getLocationsConfig(){
		return locationsConfig;
	}
	
	public static FileConfiguration getGunsConfig(){
		return gunsConfig;
	}
	
	public static FileConfiguration getArenasConfig(){
		return arenasConfig;
	}
	
	public static FileConfiguration getFileConfig(File file){
		return YamlConfiguration.loadConfiguration(file);
	}
	
	//SAVING FILES
	public static void saveConfigFile(){
		saveFile(configFile, config);
	}
	
	public static void saveLocationsFile(){
		saveFile(locationsFile, locationsConfig);
	}
	
	public static void saveGunsFile(){
		saveFile(gunsFile, gunsConfig);
	}
	
	public static void saveArenasFile(){
		saveFile(arenasFile, arenasConfig);
	}
	
	public static void saveFile(File file, FileConfiguration config){
		try{
			config.save(file);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void saveAllFiles(){
		saveConfigFile();
		saveLocationsFile();
		saveGunsFile();
		saveArenasFile();
	}
}