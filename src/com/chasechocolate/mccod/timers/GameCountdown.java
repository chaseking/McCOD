package com.chasechocolate.mccod.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;

public class GameCountdown {
	private McCOD plugin;
	private int startTime;
	
	public GameCountdown(McCOD plugin, int startTime){
		this.plugin = plugin;
		this.startTime = startTime;
	}
	
	public void startGameCountdown(){
		new BukkitRunnable(){
			int time = startTime;
			@Override
			public void run(){
				if(time == 120){
					Bukkit.broadcastMessage(ChatColor.RED + "Call of Duty will start in two minutes. Join the queue using " + ChatColor.GREEN + "/cod join" + ChatColor.RED + "!");
				} else if(time == 60){
					Bukkit.broadcastMessage(ChatColor.RED + "Call of Duty will start in one minute. Join the queue using " + ChatColor.GREEN + "/cod join" + ChatColor.RED + "!");
				} else if(time == 45 || time == 30 || time == 15 || (time <= 5 && time > 1)){
					Bukkit.broadcastMessage(ChatColor.RED + "Call of Duty will start in " + time + " seconds. Join the queue using " + ChatColor.GREEN + "/cod join" + ChatColor.RED + "!");
				} else if(time == 1){
					Bukkit.broadcastMessage(ChatColor.RED + "Call of Duty will start in 1 second. Join the queue using " + ChatColor.GREEN + "/cod join" + ChatColor.RED + "!");
				}
				time--;
				startTime--;
				if(time == 0){
					for(String playerName : plugin.gameQueue){
						Player player = Bukkit.getPlayer(playerName);
						player.sendMessage(ChatColor.RED + "Starting the game...");
					}
					//Start game
					plugin.gameManager.startGame();
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
}