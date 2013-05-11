package com.chasechocolate.mccod.timers;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.arena.Arena;

public class ArenaEndCountdown {
	private Arena arena;
	
	private int length;
	
	private McCOD plugin;
	
	public ArenaEndCountdown(Arena arena, int length){
		this.arena = arena;
		this.length = length;
		this.plugin = McCOD.getInstance();
	}
	
	public void startEndCountdown(){
		new BukkitRunnable(){
			int time = length;
			
			@Override
			public void run(){
				if(arena.getStatus() == GameStatus.INGAME){
					if(time == 1200){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 20 minutes.");
					} else if(time == 600){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 10 minutes.");
					} else if(time == 300){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 5 minutes.");
					} else if(time == 240){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 4 minutes.");
					}  else if(time == 240){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 3 minutes.");
					} else if(time == 120){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 2 minutes.");
					} else if(time == 60){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 1 minute.");
					} else if(time == 45 || time == 30 || time == 15 || (time <= 5 && time > 1)){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in " + time + " seconds.");
					} else if(time == 1){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will end in 1 second.");
					} else if(time == 0){
						arena.broadcastMessage(ChatColor.BLUE + "Ending the game on arena '" + arena.getName() + "'...");
						
						//End game
						arena.end(false);
						this.cancel();
						return;
					}
					
					time -= 1;
				} else {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(plugin, 20L, 20L);
	}
}