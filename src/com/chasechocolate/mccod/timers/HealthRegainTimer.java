package com.chasechocolate.mccod.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class HealthRegainTimer {
	private int delay;
	private int healthToAdd = 1;
	
	private McCOD plugin;
	
	public HealthRegainTimer(int delay){
		this.delay = delay;
		this.plugin = McCOD.getInstance();
	}
	
	public void startStartCountdown(){
		new BukkitRunnable(){
			@Override
			public void run(){
				if(plugin.isEnabled()){
					for(Player player : PlayerUtils.getAllActivePlayers()){
						int currentHealth = player.getHealth();
						player.setHealth(currentHealth + healthToAdd);
					}
				}
			}
		}.runTaskTimer(plugin, delay, delay);
	}
}