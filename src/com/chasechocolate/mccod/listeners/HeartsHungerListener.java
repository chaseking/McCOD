package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.chasechocolate.mccod.McCOD;

public class HeartsHungerListener implements Listener {
	private McCOD plugin;
	
	public HeartsHungerListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(plugin.inGame.contains(player.getName())){
				event.setCancelled(true);
				event.setFoodLevel(17);
			}
		}
	}
}
