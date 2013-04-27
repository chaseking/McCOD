package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import com.chasechocolate.mccod.McCOD;

public class InventoryClickListener implements Listener {
	private McCOD plugin;
	public InventoryClickListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			if(plugin.inGame.contains(player.getName())){	
				if(event.getSlotType() == SlotType.ARMOR){
					event.setCancelled(true);
				}
			}
		}
	}
}