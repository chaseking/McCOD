package com.chasechocolate.mccod.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.events.CODDeathEvent;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class EntityDamageListener implements Listener {
	private McCOD plugin;
	public EntityDamageListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player damaged = (Player) event.getEntity();
			if(event.getDamager() instanceof Player){
				Player damager = (Player) event.getDamager();
				Arena arena = (GameUtils.isInGame(damager) ? ArenaUtils.getPlayerArena(damager) : null);
				int damage = event.getDamage();
				int currentHealth = damaged.getHealth();
				int minHealth = 1;
				
				if(arena.getPlayersOnTeam(TeamColor.RED).contains(damager) && arena.getPlayersOnTeam(TeamColor.RED).contains(damaged)){
					event.setCancelled(true);
					return;
				}
				
				if(arena.getPlayersOnTeam(TeamColor.BLUE).contains(damager) && arena.getPlayersOnTeam(TeamColor.BLUE).contains(damaged)){
					event.setCancelled(true);
					return;
				}
				
				if(currentHealth - damage >= minHealth){
					event.setCancelled(true);
					
					CODDeathEvent deathEvent = new CODDeathEvent(damaged, damager);
					Bukkit.getPluginManager().callEvent(deathEvent);
				}
			}
			
			if(event.getDamager() instanceof Snowball){
				if(plugin.inGame.contains(damaged.getName())){
					event.setDamage(8);
				}
			}
		}
	}
}