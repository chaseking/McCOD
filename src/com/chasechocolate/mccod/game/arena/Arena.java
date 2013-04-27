package com.chasechocolate.mccod.game.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.effects.FireworkEffectPlayer;
import com.chasechocolate.mccod.effects.ParticleEffects;
import com.chasechocolate.mccod.game.CODPlayer;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.timers.ArenaStartCountdown;
import com.chasechocolate.mccod.utils.LocationUtils;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class Arena {
	private McCOD plugin;
	
	private String name;
	
	private Map map;
	private GameType gameType;
	private GameStatus status = GameStatus.COUNTDOWN;
	
	private int redScore = 0;
	private int blueScore = 0;
	private int min = 2;
	
	private Random random = new Random();
	
	private HashMap<String, CODPlayer> allPlayers = new HashMap<String, CODPlayer>();
	
	public Arena(McCOD plugin, String name){
		this.plugin = plugin;
		this.name = name;
	}
	
	public void startCountdown(){
		new ArenaStartCountdown(this, plugin, 120).startGameCountdown();
	}
	
	public void start(){
		boolean canStart = this.allPlayers.size() >= min;
		
		if(canStart){
			map = new Map(plugin.gameManager);
			
			for(CODPlayer player : getAllPlayers()){
				player.setTeam(getRandomTeamColor());
			}
			
			for(CODPlayer player : getPlayersOnTeam(TeamColor.RED)){
				player.getPlayer().teleport(map.getTeamSpawn(TeamColor.RED));
				player.setTeam(TeamColor.RED);
				
				player.getPlayer().sendMessage(ChatColor.RED + "You are on the red team!");
			}
			
			for(CODPlayer player : getPlayersOnTeam(TeamColor.BLUE)){
				player.getPlayer().teleport(map.getTeamSpawn(TeamColor.BLUE));
				player.setTeam(TeamColor.BLUE);
				
				player.getPlayer().sendMessage(ChatColor.BLUE + "You are on the blue team!");
			}
			
			for(CODPlayer player : getAllPlayers()){
				player.setCurrentMap(map);
				player.setPlayingGame(true);
			}
		} else {
			broadcastMessage(ChatColor.RED + "Can not start! There needs to be at least " + min + " players!");
			startCountdown();
		}
	}
	
	public void end(){
		for(CODPlayer player : getAllPlayers()){
			player.teleportToLobby();
		}
		
		String winningTeamName;
		int winningTeamPoints;
		Color color;
		Color fadeColor;
		
		//Get the winning team
		if(redScore > blueScore){
			winningTeamName = ChatColor.RED + "Red" + ChatColor.RESET + "";
			winningTeamPoints = plugin.redScore;
			color = Color.RED;
			fadeColor = Color.RED;
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
		for(CODPlayer player : getAllPlayers()){
			player.teleportToLobby();
		}
		
		//Tell the players who won
		broadcastMessage(ChatColor.GOLD + "The game has ended! Winning team: " + winningTeamName + ChatColor.GOLD + "(" + winningTeamPoints + " points)");
		
		//Add some effects to show who won
		FireworkEffectPlayer fwPlayer = new FireworkEffectPlayer();
		FireworkEffect effect = FireworkEffect.builder().withColor(color).flicker(true).withFade(fadeColor).with(Type.BALL_LARGE).build();
		
		try{
			fwPlayer.playFirework(LocationUtils.getLobbyLoc().getWorld(), LocationUtils.getLobbyLoc(), effect);
		} catch(Exception e){
			e.printStackTrace();
			plugin.log("Failed to play firework effect! Error above!");
		}
		
		for(CODPlayer codPlayer : getAllPlayers()){
			Player player = codPlayer.getPlayer();
			
			try{
				ParticleEffects.HEART.sendToPlayer(player, player.getLocation(), 0, 0, 0, 1, 10);
			} catch(Exception e){
				e.printStackTrace();
				plugin.log("Failed to play heart effect! Error above!");
			}
		}
		
		start();
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
	
	public List<CODPlayer> getAllPlayers(){
		List<CODPlayer> allPlayers = new ArrayList<CODPlayer>();
		
		for(CODPlayer player : this.allPlayers.values()){
			allPlayers.add(player);
		}
		
		return allPlayers;
	}
	
	public void addPlayer(Player player){
		CODPlayer codPlayer = PlayerUtils.getCODPlayer(player);
		this.allPlayers.put(player.getName(), codPlayer);
		
		
	}
	
	public void removePlayer(Player player){
		CODPlayer codPlayer = PlayerUtils.getCODPlayer(player);
		this.allPlayers.remove(codPlayer);
		
		codPlayer.teleportToLobby();
	}
	
	public List<CODPlayer> getPlayersOnTeam(TeamColor team){
		List<CODPlayer> onTeam = new ArrayList<CODPlayer>();
		
		for(CODPlayer player : getAllPlayers()){
			if(player.getTeam() == team){
				onTeam.add(player);
			}
		}
		
		return onTeam;
	}
	
	public Map getMap(){
		return this.map;
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
		for(CODPlayer player : getAllPlayers()){
			player.getPlayer().sendMessage(msg);
		}
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