package com.chasechocolate.mccod.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;

public class ItemConsumeListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public ItemConsumeListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if(GameUtils.isInGame(player)){
			if(item.getType() == Material.COOKED_BEEF || item.getType() == Material.MUSHROOM_SOUP){
				event.setCancelled(true);
			}
		}
	}
}