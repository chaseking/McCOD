package com.chasechocolate.mccod.scoreboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.chasechocolate.mccod.McCOD;

public class ScoreboardTools {
	private static List<Scoreboard> allScoreboards = new ArrayList<Scoreboard>();
	
	private static HashMap<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();
	
	private static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	private static McCOD plugin;
	
	public static void init(McCOD instance){
		plugin = instance;
	}
	
	public static List<Scoreboard> getScoreboards(){
		return allScoreboards;
	}
	
	public static Scoreboard createScoreboard(String name){
		Scoreboard board = manager.getNewScoreboard();
		allScoreboards.add(board);
		return board;
	}
	
	public static Scoreboard createScoreboardForPlayer(String name, Player player){
		Scoreboard board = manager.getNewScoreboard();
		board.registerNewObjective(player.getName() + "-objective", "dummy");
		
		allScoreboards.add(board);
		playerScoreboards.put(player.getName(), board);
		return board;
	}
	
	public static Scoreboard getScoreboard(Player player){
		if(playerScoreboards.containsKey(player.getName())){
			return playerScoreboards.get(player.getName());
		} else {
			return null;
		}
	}
	
	public static void update(){
		for(String playerName : plugin.inGame){
			Player player = Bukkit.getPlayer(playerName);
			Scoreboard board = getScoreboard(player);
			Objective objective = board.getObjective(playerName + "-objective");
			
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(ChatColor.RED + "" + ChatColor.GOLD + "Stats:");
			
			OfflinePlayer redScoreOffline = Bukkit.getOfflinePlayer(ChatColor.RED + "Red's Score:");
			OfflinePlayer blueScoreOffline = Bukkit.getOfflinePlayer(ChatColor.RED + "Blue's Score:");
			OfflinePlayer scoreNewLineOffline = Bukkit.getOfflinePlayer(ChatColor.BLACK + "----------");
			OfflinePlayer scoreKillsOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Kills:");
			OfflinePlayer scoreDeathsOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Deaths:");
			OfflinePlayer scoreKillstreakOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Killstreak:");
			
			Score scoreRed = objective.getScore(redScoreOffline);
			Score scoreBlue = objective.getScore(blueScoreOffline);
			Score scoreNewLine = objective.getScore(scoreNewLineOffline);
			Score scoreKills = objective.getScore(scoreKillsOffline);
			Score scoreDeaths = objective.getScore(scoreDeathsOffline);
			Score scoreKillstreak = objective.getScore(scoreKillstreakOffline);
			
			scoreRed.setScore(plugin.redScore);
			scoreNewLine.setScore(0);
			scoreBlue.setScore(plugin.blueScore);
			scoreKills.setScore(plugin.playerKills.get(playerName));
			scoreDeaths.setScore(plugin.playerDeaths.get(player.getName()));
			scoreKillstreak.setScore(plugin.killStreaks.get(player.getName()));
			
			player.setScoreboard(board);
		}
	}
}