package com.chasechocolate.mccod.game.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.game.map.MapUtils;
import com.chasechocolate.mccod.utils.Config;

public class ArenaUtils {
	private static List<Arena> allArenas = new ArrayList<Arena>();
	
	public static void loadArenas(){
		allArenas.clear();
		
		for(File mapFile : Config.getBaseMapsFile().listFiles()){
			String mapName = mapFile.getName().replace(".yml", "");
			FileConfiguration mapConfig = YamlConfiguration.loadConfiguration(mapFile);

			if(mapConfig.isString("arena")){
				String arenaName = mapConfig.getString("arena");
				Map map = MapUtils.getMap(mapName);
				Arena arena = new Arena(arenaName, map);
				
				allArenas.add(arena);
			}
		}
	}
	
	public static void addArena(Arena arena){
		if(!(allArenas.contains(arena))){			
			allArenas.add(arena);
		}
	}
	
	public static void removeArena(Arena arena){
		if(allArenas.contains(arena)){
			allArenas.remove(arena);
		}
	}
	
	public static void deleteArena(Arena arena){
		removeArena(arena);
		
		Map map = arena.getMap();
		FileConfiguration mapConfig = map.getConfig();
		if(mapConfig != null){
			if(mapConfig.isString("arena")){
				mapConfig.set("arena", null);
			}
		}
	}
	
	public static List<Arena> getAllArenas(){
		return allArenas;
	}
	
	public static Arena getPlayerArena(Player player){
		Arena playerArena = null;
		
		for(Arena arena : getAllArenas()){
			if(arena.getAllPlayers().contains(player)){
				playerArena = arena;
			}
		}
		
		return playerArena;
	}
	
	public static Arena getArena(String name){
		Arena arena = null;
		
		for(Arena allArenas : getAllArenas()){
			if(allArenas.getName().equalsIgnoreCase(name)){
				arena = allArenas;
			}
		}
		
		return arena;
	}
	
	public static boolean isArena(String name){
		boolean exists = false;
		
		for(Arena arena : getAllArenas()){
			if(arena != null && arena.getName() != null){
				if(arena.getName().equalsIgnoreCase(name)){
					exists = true;
				}
			}
		}
		
		return exists;
	}
	
	public static boolean onSameTeam(Player player1, Player player2){
		if(getPlayerArena(player1).equals(getPlayerArena(player2))){
			Arena arena = getPlayerArena(player1);
			if(arena.getPlayersOnTeam(TeamColor.RED).contains(player1) && arena.getPlayersOnTeam(TeamColor.RED).contains(player2)){
				return true;
			} else if(arena.getPlayersOnTeam(TeamColor.BLUE).contains(player1) && arena.getPlayersOnTeam(TeamColor.BLUE).contains(player2)){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean mapRunning(Map map){
		for(Arena arena : getAllArenas()){
			if(arena.getMap() == map){
				return true;
			}
		}
		
		return false;
	}
}