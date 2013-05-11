package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;

public class PlayerItemDropListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public PlayerItemDropListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		
		if(GameUtils.isInGame(player)){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You may not drop items while in the game!");
		}
	}
}