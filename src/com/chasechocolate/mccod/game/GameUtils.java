package com.chasechocolate.mccod.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.utils.Config;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class GameUtils {
	private static McCOD plugin;
	
	private static Random random = new Random();
	
	@SuppressWarnings("static-access")
	public GameUtils(McCOD plugin){
		this.plugin = plugin;
	}
	
	public static TeamColor chooseTeam(Arena arena){
		int redSize = arena.getPlayersOnTeam(TeamColor.RED).size();
		int blueSize = arena.getPlayersOnTeam(TeamColor.BLUE).size();
		boolean goOnRed = random.nextBoolean();
		
		if(redSize > blueSize){
			return TeamColor.BLUE;
		} else if(blueSize > redSize){
			return TeamColor.RED;
		} else {
			if(goOnRed){
				return TeamColor.RED;
			} else {
				return TeamColor.BLUE;
			}
		}
	}
	
	public static boolean isInGame(Player player){
		boolean isInArena = PlayerUtils.getAllActivePlayers().contains(player);
		return isInArena;
	}
	
	public static boolean lobbyExists(){
		if(Config.getLocationsConfig().isConfigurationSection("lobby")){
			return true;
		} else {
			return false;
		}
	}
	
	public static List<Map> getAllMaps(){
		if(Config.getLocationsFile().exists()){
			List<Map> allMaps = new ArrayList<Map>();
			Set<String> mapNames = Config.getLocationsConfig().getConfigurationSection("maps").getKeys(false);
			
			for(String map : mapNames){
				allMaps.add(new Map(map));
			}
			
			return allMaps;
		} else {
			return null;
		}
	}
	
	//UNUSED
	public static String getRandomMap(){
		if(Config.getLocationsFile().exists()){
			String[] maps = getAllMaps().toArray(new String[0]);
			int size = getAllMaps().size();
			int index = random.nextInt(size);
			String nextMap = maps[index];
			
			for(Arena arena : ArenaUtils.getCurrentArenas()){
				if(arena.getMap().getName().equalsIgnoreCase(nextMap)){
					return getRandomMap();
				}
			}
			
			return nextMap;
		} else {
			return null;
		}
	}
	
	public static String getMapName(Arena arena){
		String name = arena.getMap().getName();
		return name;
	}
	
	public static Location getSpawnLocation(String map, TeamColor team){
		if(Config.getLocationsFile().exists()){
			String teamColor = team.toString().toLowerCase();
			ConfigurationSection section = Config.getLocationsConfig().getConfigurationSection("maps." + map + "." + teamColor);
			
			World world = plugin.getServer().getWorld(section.getString("world"));
			int x = section.getInt("x");
			int y = section.getInt("y");
			int z = section.getInt("z");
			float yaw = Float.parseFloat(section.getString("yaw"));
			float pitch = Float.parseFloat(section.getString("pitch"));
			
			Location spawn = new Location(world, x, y, z, yaw, pitch);
			
			return spawn;
		}
		
		World world = Bukkit.getWorlds().get(0);
		Location spawn = world.getSpawnLocation();
		return spawn;
	}
}