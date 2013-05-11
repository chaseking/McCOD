package com.chasechocolate.mccod.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.chasechocolate.mccod.game.map.Map;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class WorldEditUtils {
	private static WorldEditPlugin worldEdit;
	
	@SuppressWarnings("static-access")
	public WorldEditUtils(PluginManager pm){
		Plugin worldEdit = pm.getPlugin("WorldEdit");
		
		if(worldEdit instanceof WorldEditPlugin){
			this.worldEdit = (WorldEditPlugin) worldEdit;
		}
	}
	
	public static boolean isInMap(Player player, Map map){
		return isInMap(player.getLocation(), map);
	}
	
	public static boolean isInMap(Location loc, Map map){
		Selection selection = new CuboidSelection(map.getPoint1().getWorld(), map.getPoint1(), map.getPoint2());
		return selection.contains(loc);
	}
	
	public static WorldEditPlugin getWorldEdit(){
		return worldEdit;
	}
}