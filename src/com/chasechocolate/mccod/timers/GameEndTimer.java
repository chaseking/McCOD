package com.chasechocolate.mccod.timers;

import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameStatus;

public class GameEndTimer {
	private McCOD plugin;
	private int gameLength;
	private int timeRemaining = gameLength;
	
	public GameEndTimer(McCOD plugin, int gameLength){
		this.plugin = plugin;
		this.gameLength = gameLength;
	}
	
	public void startGameEndTimer(){
		new BukkitRunnable(){
			@Override
			public void run(){
				plugin.gameManager.endGame();
			}
		}.runTaskLater(plugin, gameLength * 20);
		
		new BukkitRunnable(){
			@Override
			public void run(){
				if(plugin.gameStatus == GameStatus.INGAME){
					timeRemaining -= 1;
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
	
	public int getRemainingTime(){
		return timeRemaining;
	}
}