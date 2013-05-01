package com.chasechocolate.mccod.game.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.timers.ArenaStartCountdown;
import com.chasechocolate.mccod.utils.LocationUtils;

public class Arena {
	private McCOD plugin;
	
	private String name;
	
	private Map map;
	private GameQueue queue;
	private GameType gameType;
	private GameStatus status = GameStatus.COUNTDOWN;
	
	private int redScore = 0;
	private int blueScore = 0;
	private int min = 2;
	
	private List<String> allPlayers = new ArrayList<String>();
	private List<String> onRed = new ArrayList<String>();
	private List<String> onBlue = new ArrayList<String>();
	
	private Random random = new Random();
		
	public Arena(String name){
		this.plugin = McCOD.getInstance();
		this.name = name;
	}
	
	public void startCountdown(){
		new ArenaStartCountdown(this, plugin, 120).startGameCountdown();
	}
	
	public void start(){
		boolean canStart = this.getAllPlayers().size() >= min && map != null;
		
		if(canStart){			
			chooseTeams();
			
			for(Player player : getPlayersOnTeam(TeamColor.RED)){
				player.getPlayer().teleport(map.getTeamSpawn(TeamColor.RED));
				
				player.getPlayer().sendMessage(ChatColor.RED + "You are on the red team!");
			}
			
			for(Player player : getPlayersOnTeam(TeamColor.BLUE)){
				player.getPlayer().teleport(map.getTeamSpawn(TeamColor.BLUE));
				
				player.getPlayer().sendMessage(ChatColor.BLUE + "You are on the blue team!");
			}
		} else {
			broadcastMessage(ChatColor.RED + "Failed to start the game, restarting countdown!");
			startCountdown();
		}
	}
	
	public void end(){
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
		broadcastMessage(ChatColor.GOLD + "The game has ended! Winning team: " + winningTeamName + ChatColor.GOLD + "(" + winningTeamPoints + " points)");
		
		//Add some effects to show who won
		FireworkEffectPlayer fwPlayer = new FireworkEffectPlayer();
		FireworkEffect effect = FireworkEffect.builder().withColor(color).flicker(true).withFade(fadeColor).with(Type.BALL_LARGE).build();
		
		try{
			fwPlayer.playFirework(LocationUtils.getLobbyLoc().getWorld(), LocationUtils.getLobbyLoc(), effect);
		} catch(Exception e){
			//Do nothing
		}
		
		for(Player Player : getAllPlayers()){
			Player player = Player.getPlayer();
			
			try{
				ParticleEffects.HEART.sendToPlayer(player, player.getLocation(), 0, 0, 0, 1, 10);
			} catch(Exception e){
				//Do nothing
			}
		}
		
		startCountdown();
	}
	
	public void restart(){
		end();
		startCountdown();
	}
	
	public String getName(){
		return this.name;
	}
	
	public TeamColor getRandomTeamColor(){
		boolean randomBoolean = random.nextBoolean();
		TeamColor red = TeamColor.RED;
		TeamColor blue = TeamColor.BLUE;
		int redSize = getPlayersOnTeam(red).size();
		int blueSize = getPlayersOnTeam(blue).size();
		
		if(redSize > blueSize){
			return blue;
		} else if(blueSize > redSize){
			return red;
		} else {
			if(randomBoolean){
				return red;
			} else {
				return blue;
			}
		}
	}
	
	public void chooseTeams(){
		for(String playerName : queue.getAllInQueue()){
			Player player = Bukkit.getPlayerExact(playerName);
			TeamColor randomTeam = getRandomTeamColor();
			
			addPlayerToTeam(player, randomTeam);
		}
	}
	
	public List<Player> getAllPlayers(){
		List<Player> allPlayers = new ArrayList<Player>();
		
		for(Player player : allPlayers){
			allPlayers.add(player);
		}
		
		return allPlayers;
	}
	
	public boolean isPlaying(Player player){
		boolean isPlaying = allPlayers.contains(player.getName());
		return isPlaying;
	}
	
	public void addPlayer(Player player){
		this.allPlayers.add(player.getName());
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
		if(team == TeamColor.RED){
			List<Player> playersOnRed = new ArrayList<Player>();
			
			for(String playerName : onRed){
				Player player = Bukkit.getPlayer(playerName);
				playersOnRed.add(player);
			}
			
			return playersOnRed;		
		} else if(team == TeamColor.BLUE){
			List<Player> playersOnBlue = new ArrayList<Player>();
			
			for(String playerName : onBlue){
				Player player = Bukkit.getPlayer(playerName);
				playersOnBlue.add(player);
			}
			
			return playersOnBlue;
		} else {
			return null;
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
	
	public void broadcastMessage(String msg){
		for(Player player : getAllPlayers()){
			player.getPlayer().sendMessage(msg);
		}
	}
	
	public GameQueue getQueue(){
		return this.queue;
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