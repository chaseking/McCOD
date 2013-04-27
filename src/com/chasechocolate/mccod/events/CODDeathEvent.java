package com.chasechocolate.mccod.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CODDeathEvent extends Event {
	private Player player;
	private Player killer;
	
	private static final HandlerList handlers = new HandlerList();
	
	public CODDeathEvent(Player player, Player killer){
		this.player = player;
		this.killer = killer;
	}
	
	public CODDeathEvent(Player player){
		this.player = player;
		this.killer = null;
	}

	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Player getKiller(){
		return this.killer;
	}
}