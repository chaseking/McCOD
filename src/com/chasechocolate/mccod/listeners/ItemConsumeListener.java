package com.chasechocolate.mccod.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import com.chasechocolate.mccod.McCOD;

public class ItemConsumeListener implements Listener {
	private McCOD plugin;
	
	public ItemConsumeListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if(plugin.inGame.contains(player.getName())){
			if(item.getType() == Material.COOKED_BEEF || item.getType() == Material.MUSHROOM_SOUP){
				event.setCancelled(true);
			}
		}
	}
}
