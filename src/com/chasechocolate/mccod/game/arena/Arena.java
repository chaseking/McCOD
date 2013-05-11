package com.chasechocolate.mccod.game.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.effects.FireworkEffectPlayer;
import com.chasechocolate.mccod.effects.ParticleEffects;
import com.chasechocolate.mccod.game.GameQueue;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;
import com.chasechocolate.mccod.timers.ArenaEndCountdown;
import com.chasechocolate.mccod.timers.ArenaStartCountdown;
import com.chasechocolate.mccod.utils.LocationUtils;
import com.chasechocolate.mccod.utils.PlayerUtils;
import com.chasechocolate.mccod.utils.Utilities;

public class Arena {
	private McCOD plugin;

	private String name;
	
	private List<String> allPlayers = new ArrayList<String>();
	private List<String> onRed = new ArrayList<String>();
	private List<String> onBlue = new ArrayList<String>();

	private int redScore = 0;
	private int blueScore = 0;
	private int min = 1;
	
	private Map map;
	private GameQueue queue;
	private GameType gameType;
	private GameStatus status = GameStatus.COUNTDOWN;
	
	public Arena(String name, Map map){
		this.name = name;
		this.map = map;
		this.queue = new GameQueue(this);
		this.plugin = McCOD.getInstance();
		
		if(this.map.getConfigFile().exists()){
			this.map.getConfig().set("arena", this.name);
		}
	}
	
	public void startCountdown(){
		new ArenaStartCountdown(this, 120).startStartCountdown();
		
		this.status = GameStatus.COUNTDOWN;
	}
	
	public void start(){
		boolean canStart = (allPlayers.size() >= min || queue.getAllInQueue().size() >= min) && map != null;
		
		if(canStart){
			//Choose the teams
			chooseTeams();
			
			//Set game type and status
			gameType = GameUtils.getRandomGameType();
			status = GameStatus.INGAME;
			
			//Move the players in the queue to the actual game
			allPlayers.clear();
			
			for(String playerName : queue.getAllInQueue()){
				allPlayers.add(playerName);
			}
			
			//Clear all in queue
			queue.getAllInQueue().clear();
						
			//Teleport red players and inform them that they are on the red team
			for(Player player : getPlayersOnTeam(TeamColor.RED)){
				//player.teleport(map.getTeamSpawn(TeamColor.RED));
				PlayerUtils.respawn(player);
				player.sendMessage(ChatColor.RED + "You are on the red team!");
			}
			
			//Teleport blue players and inform them that they are on the blue team
			for(Player player : getPlayersOnTeam(TeamColor.BLUE)){
				//player.teleport(map.getTeamSpawn(TeamColor.BLUE));
				PlayerUtils.respawn(player);
				player.sendMessage(ChatColor.BLUE + "You are on the blue team!");
			}
			
			//Show player scoreboards
			ScoreboardTools.update();
			
			//Inform all players that the game has started, and tell them on which game type, which map and how many players are playing
			broadcastMessage(ChatColor.AQUA + "Game started! " + ChatColor.GREEN + "Type: " + ChatColor.DARK_AQUA + Utilities.capitalize(gameType.toString().replaceAll("_", " ")) + ChatColor.GREEN + " | Map: " + ChatColor.DARK_AQUA + map.getName() + ChatColor.GREEN + " | Total players: " + ChatColor.DARK_AQUA + allPlayers.size());
			
			//Start ending timers
			if(gameType == GameType.DEATHMATCH){
				new ArenaEndCountdown(this, plugin.lengthDeathmatch).startEndCountdown();
			} else {
				new ArenaEndCountdown(this, 600).startEndCountdown();
			}
		} else {
			broadcastMessage(ChatColor.RED + "Failed to start the game, restarting countdown!");
			startCountdown();
		}
	}
	
	public void end(boolean force){
		this.status = GameStatus.ENDING;
		
		String winningTeamName;
		int winningTeamPoints;
		
		Color color;
		Color fadeColor;
		
		//Get the winning team
		if(redScore > blueScore){
			winningTeamName = ChatColor.RED + "Red" + ChatColor.RESET + "";
			winningTeamPoints = redScore;
			color = Color.RED;
			fadeColor = Color.MAROON;
		} else if(blueScore > redScore){
			winningTeamName = ChatColor.BLUE + "Blue" + ChatColor.RESET + "";
			winningTeamPoints = blueScore;
			color = Color.BLUE;
			fadeColor = Color.AQUA;
		} else {
			winningTeamName = ChatColor.LIGHT_PURPLE + "Draw" + ChatColor.RESET + "";
			winningTeamPoints = redScore;
			color = Color.FUCHSIA;
			fadeColor = Color.PURPLE;
		}
		
		//Teleport the players to the lobby
		for(Player player : getAllPlayers()){
			player.teleport(LocationUtils.getLobbyLoc());
		}
		
		//Tell the players who won
		broadcastMessage(ChatColor.GOLD + "The game has" + (force ? " force " : " ") + "ended! Winning team: " + winningTeamName + " " + ChatColor.GOLD + "(" + winningTeamPoints + " points)");
		
		//Add some effects to show who won
		FireworkEffectPlayer fwPlayer = new FireworkEffectPlayer();
		FireworkEffect effect = FireworkEffect.builder().withColor(color).flicker(true).withFade(fadeColor).with(Type.BALL_LARGE).build();
		
		try{
			fwPlayer.playFirework(LocationUtils.getLobbyLoc().getWorld(), LocationUtils.getLobbyLoc(), effect);
		} catch(Exception e){
			//Do nothing
		}
		
		for(Player player : getAllPlayers()){
			try{
				ParticleEffects.HEART.sendToPlayer(player, player.getLocation(), 0, 0, 0, 1, 10);
			} catch(Exception e){
				//Do nothing
			}
		}
		
		for(Player player : getAllPlayers()){
			ScoreboardTools.removeScoreboard(player);
			PlayerUtils.wipe(player);
		}
		
		allPlayers.clear();
		
		startCountdown();
	}
	
	public void restart(){
		end(false);
		startCountdown();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void chooseTeams(){
		for(String playerName : queue.getAllInQueue()){
			Player player = Bukkit.getPlayerExact(playerName);
			TeamColor randomTeam = GameUtils.getRandomTeamColor(this);
			
			addPlayerToTeam(player, randomTeam);
		}
	}
	
	public List<Player> getAllPlayers(){
		List<Player> allPlayers = new ArrayList<Player>();
		
		for(String playerName : this.allPlayers){
			allPlayers.add(Bukkit.getPlayerExact(playerName));
		}
		
		return allPlayers;
	}
	
	public boolean isPlaying(Player player){
		boolean isPlaying = allPlayers.contains(player.getName());
		return isPlaying;
	}
	
	public void addPlayer(Player player){
		if(!(isPlaying(player))){
			if(status == GameStatus.INGAME){
				allPlayers.add(player.getName());
				
				TeamColor team = GameUtils.chooseTeam(this);
				if(team == TeamColor.RED){
					onRed.add(player.getName());
				} else if(team == TeamColor.BLUE){
					onBlue.add(player.getName());
				}
				
				PlayerUtils.respawn(player);
			} else {
				if(!(queue.isInQueue(player))){
					queue.addToQueue(player);
					player.sendMessage(ChatColor.GREEN + "You have been added to the queue of this arena! You will be teleported when the game starts!");					
				} else {
					player.sendMessage(ChatColor.RED + "You are already in the game queue!");
				}
			}
		} else {
			player.sendMessage(ChatColor.RED + "You are already playing in the arena!");
		}
	}
	
	public void removePlayer(Player player){
		if(isPlaying(player)){
			this.allPlayers.remove(player);
			player.teleport(LocationUtils.getLobbyLoc());
		}
		
		if(onRed.contains(player.getName())){
			onRed.remove(player.getName());
		}
		
		if(onBlue.contains(player.getName())){
			onBlue.remove(player.getName());
		}
	}
	
	public void addPlayerToTeam(Player player, TeamColor team){
		if(team == TeamColor.RED){
			onRed.add(player.getName());
		} else if(team == TeamColor.BLUE){
			onBlue.add(player.getName());
		}
	}
	
	public List<Player> getPlayersOnTeam(TeamColor team){
		List<Player> playersOnTeam = new ArrayList<Player>();
		
		if(team == TeamColor.RED){			
			for(String playerName : onRed){
				Player player = Bukkit.getPlayer(playerName);
				playersOnTeam.add(player);
			}	
		} else if(team == TeamColor.BLUE){
			for(String playerName : onBlue){
				Player player = Bukkit.getPlayer(playerName);
				playersOnTeam.add(player);
			}
		}
		
		return playersOnTeam;
	}
	
	public void broadcastMessage(String msg){
		for(Player player : getAllPlayers()){
			player.sendMessage(msg);
		}
		
		for(String playerName : queue.getAllInQueue()){
			Player player = Bukkit.getPlayerExact(playerName);
			player.sendMessage(msg);
		}
	}
	
	public Map getMap(){
		return this.map;
	}
	
	public void setMap(Map map){
		this.map = map;
	}
	
	public int getRedScore(){
		return this.redScore;
	}
	
	public void setRedScore(int score){
		this.redScore = score;
	}
	
	public int getBlueScore(){
		return this.blueScore;
	}
	
	public void setBlueScore(int score){
		this.blueScore = score;
	}
	
	public GameQueue getQueue(){
		return this.queue;
	}
	
	public void setQueue(GameQueue queue){
		this.queue = queue;
	}
	
	public GameStatus getStatus(){
		return this.status;
	}
	
	public void setGameStatus(GameStatus status){
		this.status = status;
	}
	
	public GameType getGameType(){
		return this.gameType;
	}
	
	public void setGameType(GameType gameType){
		this.gameType = gameType;
	}
}