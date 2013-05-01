package com.chasechocolate.mccod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class TagListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public TagListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerReceiveTag(PlayerReceiveNameTagEvent event){
		Player seen = event.getNamedPlayer();
		
		if(GameUtils.isInGame(seen)){
			event.setTag(PlayerUtils.getTeam(seen).toChatColor() + seen.getName());
		}
	}
}