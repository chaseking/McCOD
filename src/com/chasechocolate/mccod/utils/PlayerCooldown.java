package com.chasechocolate.mccod.utils;

public class PlayerCooldown {
	private long startTime;
	private String playerName;
	private String cooldownName;
	private long lengthInMillis;
	private long endTime;

	public PlayerCooldown(String cooldownName, String playerName, long lengthInMillis){
		this.cooldownName = cooldownName;
		this.startTime = System.currentTimeMillis();
		this.playerName = playerName;
		this.lengthInMillis = lengthInMillis;
		this.endTime = startTime + this.lengthInMillis;
	}

	public boolean isOver(){
		return endTime < System.currentTimeMillis();
	}

	public int getTimeLeft(){
		return (int) (this.endTime - System.currentTimeMillis());
	}

	public String getPlayerName(){
		return this.playerName;
	}

	public String getCooldownName(){
		return this.cooldownName;
	}

	public void reset(){
		startTime = System.currentTimeMillis();
		endTime = startTime + lengthInMillis;
	}
}