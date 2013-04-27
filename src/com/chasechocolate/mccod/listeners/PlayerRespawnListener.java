package com.chasechocolate.mccod.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.TeamColor;

public class PlayerRespawnListener implements Listener {
	private McCOD plugin;
	public PlayerRespawnListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if(plugin.inGame.contains(player.getName())){
			if(plugin.onRed.contains(player.getName())){
				Location redSpawn = plugin.gameManager.getSpawnLocation(plugin.currentMap, TeamColor.RED);
				event.setRespawnLocation(redSpawn);
			} else if(plugin.onBlue.contains(player.getName())){
				Location blueSpawn = plugin.gameManager.getSpawnLocation(plugin.currentMap, TeamColor.BLUE);
				event.setRespawnLocation(blueSpawn);
			}
		}
	}
}