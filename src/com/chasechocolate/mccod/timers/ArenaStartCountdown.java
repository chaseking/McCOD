package com.chasechocolate.mccod.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.arena.Arena;

public class ArenaStartCountdown {
	private Arena arena;
	
	private McCOD plugin;
	
	private int startTime;
	
	public ArenaStartCountdown(Arena arena, McCOD plugin, int startTime){
		this.arena = arena;
		this.plugin = plugin;
		this.startTime = startTime;
	}
	
	public void startGameCountdown(){
		new BukkitRunnable(){
			int time = startTime;
			
			@Override
			public void run(){
				if(time == 120){
					Bukkit.broadcastMessage(ChatColor.RED + "Arena '" + arena.getName() + "' will start in two minutes.");
				} else if(time == 60){
					Bukkit.broadcastMessage(ChatColor.RED + "Arena '" + arena.getName() + "' will start in one minute.");
				} else if(time == 45 || time == 30 || time == 15 || (time <= 5 && time > 1)){
					Bukkit.broadcastMessage(ChatColor.RED + "Arena '" + arena.getName() + "' will start in " + time + " seconds.");
				} else if(time == 1){
					Bukkit.broadcastMessage(ChatColor.RED + "Arena '" + arena.getName() + "' will start in 1 second.");
				}
				
				time--;
				startTime--;
				
				if(time == 0){
					arena.broadcastMessage(ChatColor.RED + "Starting the game...");
					
					//Start game
					arena.start();
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
}