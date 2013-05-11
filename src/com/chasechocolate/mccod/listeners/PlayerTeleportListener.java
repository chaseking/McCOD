package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.utils.PlayerUtils;
import com.chasechocolate.mccod.utils.WorldEditUtils;

public class PlayerTeleportListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public PlayerTeleportListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		Location to = event.getTo();
		
		if(GameUtils.isInGame(player)){
			for(Arena arena : ArenaUtils.getAllArenas()){
				Map map = arena.getMap();
				if(!(WorldEditUtils.isInMap(to, map))){
					PlayerUtils.respawn(player);
					player.sendMessage(ChatColor.RED + "You respawned because you exited the map! Please use /cod leave to leave games!");
				}
			}
		}
	}
}