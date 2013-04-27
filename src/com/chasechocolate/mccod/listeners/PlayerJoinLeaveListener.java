package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.chasechocolate.mccod.McCOD;

public class PlayerJoinLeaveListener implements Listener {
	private McCOD plugin;
	public PlayerJoinLeaveListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		plugin.gameManager.removeFromGame(player);
	}
}