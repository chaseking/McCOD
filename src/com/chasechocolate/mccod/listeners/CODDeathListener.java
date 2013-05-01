package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.events.CODDeathEvent;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;
import com.chasechocolate.mccod.utils.PlayerStats;
import com.chasechocolate.mccod.utils.PlayerUtils;
import com.chasechocolate.mccod.utils.StatUtils;

public class CODDeathListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public CODDeathListener(McCOD plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onCODDeath(CODDeathEvent event){
		Player player = event.getPlayer();
		Player killer = event.getKiller();
		
		Arena arena = ArenaUtils.getPlayerArena(player);
		
		PlayerUtils.respawn(player);
		
		if(arena.getGameType() == GameType.DEATHMATCH){
			if(PlayerUtils.getTeam(player) == TeamColor.BLUE){
				arena.setRedScore(arena.getRedScore() + 1);
			} else if(PlayerUtils.getTeam(killer) == TeamColor.RED){
				arena.setBlueScore(arena.getBlueScore() + 1);
			}
			
			if(killer != null){ //They were killed by a player
				PlayerStats playerStats = StatUtils.getPlayerStats(player);
				PlayerStats killerStats = StatUtils.getPlayerStats(killer);
				
				if(playerStats.getCurrentKillstreak() > 5){
					arena.broadcastMessage(PlayerUtils.getTeam(killer).toChatColor() + "" + killer.getName() + ChatColor.AQUA + " has just ended " + PlayerUtils.getTeam(player).toChatColor() + "" + player.getName() + "'s " + playerStats.getCurrentKillstreak() + " killstreak!");
				}
				
				//Managing the kills/deaths
				playerStats.manageDeath();
				killerStats.manageKill();
				
				if(killerStats.getCurrentKillstreak() % 5 == 0){
					arena.broadcastMessage(PlayerUtils.getTeam(killer).toChatColor() + "" + killer.getName() + "" + ChatColor.RESET + "" + ChatColor.AQUA + " is at a " + killerStats.getCurrentKillstreak() + " killstreak!");
				}
			} else { //They died from other causes
				PlayerStats playerStats = StatUtils.getPlayerStats(player);
				
				if(playerStats.getCurrentKillstreak() > 5){
					arena.broadcastMessage(PlayerUtils.getTeam(player).toChatColor() + "" + player.getName() + "" + ChatColor.AQUA + " has lost their " + playerStats.getCurrentKillstreak() + " killstreak!");
				}
				
				playerStats.manageDeath();
			}
			
			ScoreboardTools.update();
		}
	}
}