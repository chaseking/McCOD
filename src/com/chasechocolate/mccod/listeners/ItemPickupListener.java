package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.chasechocolate.mccod.McCOD;

public class ItemPickupListener implements Listener {
	private McCOD plugin;
	public ItemPickupListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Item item = event.getItem();
		if(plugin.droppedItems.contains(item.getUniqueId())){
			event.setCancelled(true);
		}
	}
}