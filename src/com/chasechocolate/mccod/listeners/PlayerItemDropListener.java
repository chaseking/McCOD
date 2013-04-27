package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.chasechocolate.mccod.McCOD;

public class PlayerItemDropListener implements Listener {
	private McCOD plugin;
	public PlayerItemDropListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if(plugin.inGame.contains(player.getName())){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You may not drop items while in the game!");
		}
	}
}