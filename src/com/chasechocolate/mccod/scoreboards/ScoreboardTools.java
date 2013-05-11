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
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.utils.PlayerStats;
import com.chasechocolate.mccod.utils.StatUtils;

public class ScoreboardTools {
	private static List<Scoreboard> allScoreboards = new ArrayList<Scoreboard>();
	
	private static HashMap<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();
	
	private static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	@SuppressWarnings("unused")
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
	
	public static Scoreboard createScoreboardForPlayer(Player player){
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
	
	public static Scoreboard getBlankScoreboard(){
		Scoreboard board = manager.getNewScoreboard();
		return board;
	}
	
	public static void update(){
		for(Arena arena : ArenaUtils.getAllArenas()){
			for(Player player : arena.getAllPlayers()){
				PlayerStats stats = StatUtils.getPlayerStats(player);
				
				Scoreboard board = getScoreboard(player);
				
				if(board ==  null){
					board = createScoreboardForPlayer(player);
				}
				
				Objective objective = board.getObjective(player.getName() + "-objective");
				
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				objective.setDisplayName(ChatColor.RED + "" + ChatColor.GOLD + "  Stats:  ");
				
				OfflinePlayer redScoreOffline = Bukkit.getOfflinePlayer(ChatColor.RED + "Red's Score:");
				OfflinePlayer blueScoreOffline = Bukkit.getOfflinePlayer(ChatColor.BLUE + "Blue's Score:");
				OfflinePlayer scoreKillsOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Kills:");
				OfflinePlayer scoreDeathsOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Deaths:");
				OfflinePlayer scoreKillstreakOffline = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Killstreak:");
				
				Score scoreRed = objective.getScore(redScoreOffline);
				Score scoreBlue = objective.getScore(blueScoreOffline);
				Score scoreKills = objective.getScore(scoreKillsOffline);
				Score scoreDeaths = objective.getScore(scoreDeathsOffline);
				Score scoreKillstreak = objective.getScore(scoreKillstreakOffline);
				
				scoreRed.setScore(arena.getRedScore());
				scoreBlue.setScore(arena.getBlueScore());
				scoreKills.setScore(stats.getKills());
				scoreDeaths.setScore(stats.getDeaths());
				scoreKillstreak.setScore(stats.getCurrentKillstreak());
				
				player.setScoreboard(board);
			}
		}
	}
	
	public static void removeScoreboard(Player player){
		player.setScoreboard(getBlankScoreboard());
		
		if(playerScoreboards.containsKey(player.getName())){
			playerScoreboards.remove(player.getName());
		}
	}
}