package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;

public class PlayerDeathListener implements Listener {
	private McCOD plugin;
	public PlayerDeathListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		if(plugin.inGame.contains(player.getName()) && plugin.inGame.contains(killer.getName())){
			if(plugin.gameType == GameType.DEATHMATCH){
				if(plugin.onRed.contains(killer.getName())){
					plugin.redScore += 1;
				} else if(plugin.onBlue.contains(killer.getName())){
					plugin.blueScore += 1;
				}
				
				if(killer != null){ //They were killed by a player
					//Getting the killer's kills/deaths/killstreaks
					int killerKills = plugin.playerKills.get(killer.getName());
					int killerDeaths = plugin.playerDeaths.get(killer.getName());
					int killerKillstreak = plugin.killStreaks.get(killer.getName());
					
					player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 20F, 0F);
					
					//Incrememting kills
					plugin.playerKills.put(killer.getName(), killerKills + 1);

					if(killerKillstreak % 5 == 0){
						plugin.gameManager.sendGameMessage(plugin.gameManager.getChatColor(player) + player.getName() + "" + ChatColor.RESET + "" + ChatColor.AQUA + " is at a " + killerKillstreak + " killstreak!");
					}
					
					//Getting the player's kills/deaths/killstreaks
					int playerKills = plugin.playerKills.get(player.getName());
					int playerDeaths = plugin.playerDeaths.get(player.getName());
					int playerKillstreak = plugin.killStreaks.get(player.getName());
					
					if(playerKillstreak > 5){
						plugin.gameManager.sendGameMessage(plugin.gameManager.getChatColor(killer) + killer.getName() + ChatColor.AQUA + " has just ended " + player.getName() + "'s " + playerKillstreak + " killstreak!");
					}
					
					plugin.playerDeaths.put(player.getName(), playerDeaths + 1);
					plugin.killStreaks.put(player.getName(), 0);
				} else { //They died from other causes
					int playerKillstreak = plugin.killStreaks.get(player.getName());
					
					if(playerKillstreak > 5){
						plugin.gameManager.sendGameMessage(plugin.gameManager.getChatColor(player) + "" + player.getName() + "" + ChatColor.AQUA + " has lost their " + playerKillstreak + " killstreak!");
					}
				}
				
				ScoreboardTools.update();
			}
		}
	}
}