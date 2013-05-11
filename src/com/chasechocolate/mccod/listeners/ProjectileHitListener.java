package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.chasechocolate.mccod.McCOD;

public class ProjectileHitListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public ProjectileHitListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		Projectile proj = event.getEntity();
		
		if(proj instanceof Snowball){
			
		}
	}
}