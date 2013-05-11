package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.utils.LocationUtils;
import com.chasechocolate.mccod.utils.WorldEditUtils;

public class PlayerJoinLeaveListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public PlayerJoinLeaveListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		if(GameUtils.isInGame(player)){
			Arena arena = ArenaUtils.getPlayerArena(player);
			arena.removePlayer(player);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){		
		Player player = event.getPlayer();
		
		for(Arena arena : ArenaUtils.getAllArenas()){
			if(WorldEditUtils.isInMap(player, arena.getMap())){
				if(!(player.isOp())){
					player.teleport(LocationUtils.getLobbyLoc());
					player.sendMessage(ChatColor.RED + "You were teleported to the lobby because you joined in a game map!");
				}
			}
		}
	}
}