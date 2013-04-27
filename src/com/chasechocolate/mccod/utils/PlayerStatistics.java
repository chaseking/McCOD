package com.chasechocolate.mccod.utils;

import java.sql.ResultSet;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.mysql.SQLUtils;

public class PlayerStatistics {
	private String playerName;
	
	private boolean isLoaded;
	
	private int kills;
	private int deaths;
	private int highestKillstreak;
	private int gamesPlayed;
	private int timesOnRed;
	private int timesOnBlue;
	private int timeInGame;
	
	private static McCOD plugin;
	
	public PlayerStatistics(Player player){
		this.playerName = player.getName();
	}
	
	public static void init(McCOD instance){
		plugin = instance;
	}
	
	public void load(){
		new BukkitRunnable(){
			@Override
			public void run(){
				isLoaded = false;
				ResultSet rs = SQLUtils.getResultSet("SELECT * FROM player_stats WHERE USERNAME = '" + playerName + "'");
				
				try{
					while(rs.next()){
						isLoaded = true;
						
						kills = Integer.parseInt(rs.getString("total_kills"));
						deaths = Integer.parseInt(rs.getString("total_deaths"));
						highestKillstreak = Integer.parseInt(rs.getString("highest_killstreak"));
						gamesPlayed = Integer.parseInt(rs.getString("games_played"));
						timesOnRed = Integer.parseInt(rs.getString("times_on_red"));
						timesOnBlue = Integer.parseInt(rs.getString("times_on_blue"));
						timeInGame = Integer.parseInt(rs.getString("time_in_game"));
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				
				if(!(isLoaded)){
					SQLUtils.executeUpdate("INSERT INTO player_stats(username, total_kills, total_deaths, highest_killstreak, games_played, times_on_red, times_on_blue, time_in_game) VALUES ('" + playerName + "', 0, 0, 0, 0, 0, 0, 0");
				}
			}
		}.runTaskLaterAsynchronously(plugin, 20L);
	}
	
	public void pushStats(){
		new BukkitRunnable(){
			@Override
			public void run(){
				ResultSet rs = SQLUtils.getResultSet("SELECT * FROM player_stats WHERE USERNAME = '" + playerName + "'");
				
				if(!(isLoaded)){
					try{
						while(rs.next()){
							rs.updateString("total_kills", kills + "");
							rs.updateString("total_deaths", deaths + "");
							rs.updateString("highest_killstreak", highestKillstreak + "");
							rs.updateString("games_played", gamesPlayed + "");
							rs.updateString("times_on_red", timesOnRed + "");
							rs.updateString("times_on_blue", timesOnBlue + "");
							rs.updateString("time_in_game", timeInGame + "");
							
							rs.updateRow();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				} else {
					return;
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void addGameStats(Arena arena){
		
	}
}