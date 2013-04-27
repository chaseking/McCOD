package com.chasechocolate.mccod.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class Teleporter {
	private String owner;
	
	private boolean coolingDown;
	private boolean isConnected;
	
	private Location loc1;
	private Location loc2;
	
	private BlockState oldState1;
	private BlockState oldState1Up;
	private BlockState oldState2;
	private BlockState oldState2Up;
	
	public Teleporter(String owner, Location loc1){
		this.owner = owner;
		this.loc1 = loc1;
		this.oldState1 = loc1.getBlock().getState();
		this.oldState1Up = loc1.add(0, 1, 0).getBlock().getState();
	}
	
	public void construct(){
		loc1.getBlock().setType(Material.WORKBENCH);
		loc1.add(0, 1, 0).getBlock().setType(loc2 != null ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF);
	}
	
	public void constructConnected(){
		if(loc2 != null){
			loc2.getBlock().setType(Material.WORKBENCH);
			loc2.add(0, 1, 0).getBlock().setType(loc1 != null ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF);
		}
	}
	
	public void destroy(){
		if(loc1 != null){
			loc1.getBlock().setType(oldState1.getType());
			loc1.add(0, 1, 0).getBlock().setType(oldState1Up.getType());
		}
		
		if(loc2 != null && isConnected()){
			loc2.getBlock().setType(oldState2.getType());
			loc2.add(0, 1, 0).getBlock().setType(oldState2Up.getType());
		}
	}
	
	public void setConnectedTeleporter(Location loc2){
		this.loc2 = loc2;
		this.oldState2 = loc2.getBlock().getState();
		this.oldState2Up = loc2.add(0, 1, 0).getBlock().getState();
	}
	
	public void teleport(Player player){
		if(isConnected()){
			if(isCoolingDown()){
				//It's cooling down
			} else {
				if(player.getLocation().subtract(0, 1, 0).equals(loc1)){
					player.teleport(loc2);
				} else if(player.getLocation().subtract(0, 1, 0).equals(loc2)){
					player.teleport(loc1);
				}
			}
		} else {
			//There is only 1 teleporter
		}
	}
	public boolean isConnected(){
		return isConnected;
	}
	
	public boolean isCoolingDown(){
		return coolingDown;
	}
	
	public String getOwner(){
		return this.owner;
	}
}