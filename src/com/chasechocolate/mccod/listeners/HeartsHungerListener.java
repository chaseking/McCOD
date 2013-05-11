package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;

public class HeartsHungerListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public HeartsHungerListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			
			if(GameUtils.isInGame(player)){
				event.setCancelled(true);
				event.setFoodLevel(20);
			}
		}
	}
	
	@EventHandler
	public void onHealthChange(EntityRegainHealthEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			
			if(GameUtils.isInGame(player)){
				event.setCancelled(true);
			}
		}
	}
}