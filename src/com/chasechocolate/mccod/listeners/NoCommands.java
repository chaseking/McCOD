package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.chasechocolate.mccod.McCOD;

public class NoCommands implements Listener {
	private McCOD plugin;
	public NoCommands(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		String msg = event.getMessage();
		if(plugin.inGame.contains(player.getName())){
			if(!(msg.startsWith("/cod") || msg.startsWith("/callofduty"))){
				if(!(player.isOp())){
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You may only use McCOD commands while in-game (e.g. /cod leave)!");
				}
			}
		}
	}
}