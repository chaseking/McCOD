package com.chasechocolate.mccod.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.chasechocolate.mccod.events.CODDeathEvent;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class CODDeathListener implements Listener {
	@EventHandler
	public void onCODDeath(CODDeathEvent event){
		Player player = event.getPlayer();
		
		Player killer = event.getKiller();
		
		Arena arena = ArenaUtils.getPlayerArena(player);
		Map map = arena.getMap();
		Location spawn = map.getTeamSpawn(PlayerUtils.getCODPlayer(player).getTeam());
		
		player.teleport(spawn);
		PlayerUtils.restoreInventory(player);
		
		if(killer != null){
			
		}
	}
}