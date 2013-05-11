package com.chasechocolate.mccod.timers;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.arena.Arena;

public class ArenaStartCountdown {
	private Arena arena;
	
	private int startTime;
	
	private McCOD plugin;
	
	public ArenaStartCountdown(Arena arena, int startTime){
		this.arena = arena;
		this.startTime = startTime;
		this.plugin = McCOD.getInstance();
	}
	
	public void startStartCountdown(){
		new BukkitRunnable(){			
			int time = startTime;

			@Override
			public void run(){
				if(arena.getStatus() == GameStatus.COUNTDOWN){
					if(time == 120){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will start in two minutes.");
					} else if(time == 60){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will start in one minute.");
					} else if(time == 45 || time == 30 || time == 15 || (time <= 5 && time > 1)){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will start in " + time + " seconds.");
					} else if(time == 1){
						arena.broadcastMessage(ChatColor.BLUE + "Arena '" + arena.getName() + "' will start in 1 second.");
					} else if(time == 0){
						arena.broadcastMessage(ChatColor.BLUE + "Attempting to start the game...");
						
						//Start game
						arena.start();
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