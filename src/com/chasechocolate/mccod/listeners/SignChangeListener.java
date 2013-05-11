package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.utils.Localization;

public class SignChangeListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public SignChangeListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		Player player = event.getPlayer();
		
		if(player.hasPermission(Localization.SIGN_PERM)){
			if(event.getLine(0).equalsIgnoreCase("[McCOD]")){
				if(event.getLine(1).equalsIgnoreCase("Join")){
					if(ArenaUtils.isArena(event.getLine(2))){
						event.setLine(0, ChatColor.GOLD + "[McCOD]");
						event.setLine(1, ChatColor.AQUA + "Join");
						event.setLine(2, ChatColor.GREEN + "" + event.getLine(2));
						player.sendMessage(ChatColor.GREEN + "Successfully created a join sign for the arena '" + ChatColor.stripColor(event.getLine(2)) + "'!");
					} else {
						player.sendMessage(ChatColor.RED + "Could not find the arena '" + event.getLine(2) + "'!");
					}
				}
			}
		}
	}
}