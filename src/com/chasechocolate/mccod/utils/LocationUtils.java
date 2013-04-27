package com.chasechocolate.mccod.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.chasechocolate.mccod.McCOD;

public class LocationUtils {
	private static McCOD plugin;
	
	@SuppressWarnings("static-access")
	public LocationUtils(McCOD plugin){
		this.plugin = plugin;
	}
	
	public static Location getLobbyLoc(){
		if(plugin.locationsFile.exists()){
			World world = Bukkit.getWorld(plugin.locationsConfiguration.getString("lobby.world"));
			int x = plugin.locationsConfiguration.getInt("lobby.x");
			int y = plugin.locationsConfiguration.getInt("lobby.y");
			int z = plugin.locationsConfiguration.getInt("lobby.z");
			float yaw = Float.parseFloat(plugin.locationsConfiguration.getString("lobby.yaw"));
			float pitch = Float.parseFloat(plugin.locationsConfiguration.getString("lobby.pitch"));
			
			Location lobby = new Location(world, x, y, z, yaw, pitch);
			
			return lobby;
		} else {
			Location lobby = Bukkit.getWorlds().get(0).getSpawnLocation();
			return lobby;
		}
	}
}