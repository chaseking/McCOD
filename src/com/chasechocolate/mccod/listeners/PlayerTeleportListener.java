package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.chasechocolate.mccod.McCOD;

public class PlayerTeleportListener implements Listener {
	private McCOD plugin;
	public PlayerTeleportListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		if(plugin.inGame.contains(player.getName())){
			//TODO
		}
	}
}