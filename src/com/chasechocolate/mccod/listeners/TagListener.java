package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.chasechocolate.mccod.McCOD;

public class TagListener implements Listener {
	private McCOD plugin;
	public TagListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerReceiveTag(PlayerReceiveNameTagEvent event){
		Player seen = event.getNamedPlayer();
		
		if(plugin.inGame.contains(seen.getName())){
			event.setTag(plugin.gameManager.getChatColor(seen) + seen.getName());
		}
	}
}