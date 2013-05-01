package com.chasechocolate.mccod.game;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.effects.FireworkEffectPlayer;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;
import com.chasechocolate.mccod.timers.GameCountdown;
import com.chasechocolate.mccod.timers.GameEndTimer;
import com.chasechocolate.mccod.utils.Localization;

public class GameManager {
	private McCOD plugin;
	
	private Random random = new Random();
	
	public GameManager(McCOD plugin){
		this.plugin = plugin;
	}
	
	/**
	 * NOW AN OBSELETE CLASS! I AM ONLY SAVING IT TO MOVE SOME OF THE METHODS IN THIS CLASS!
	 */
	
	public void startGame(){
		if(canStart()){
			String mapName = getRandomMap();
			Location redSpawn = getSpawnLocation(mapName, TeamColor.RED);
			Location blueSpawn = getSpawnLocation(mapName, TeamColor.BLUE);
			GameType type = getNextGameType();
			
			chooseTeams(plugin.gameQueue);
			
			plugin.redScore = 0;
			plugin.blueScore = 0;
			plugin.killStreaks.clear();
			plugin.playerKills.clear();
			plugin.playerDeaths.clear();
			plugin.gameStatus = GameStatus.INGAME;
			plugin.inGame = plugin.gameQueue;
			
			//Teleporting red team
			for(String playerName : plugin.onRed){
				Player player = Bukkit.getPlayer(playerName);
				player.teleport(redSpawn);
			}
			
			//Teleporting blue team
			for(String playerName : plugin.onBlue){
				Player player = Bukkit.getPlayer(playerName);
				player.teleport(blueSpawn);
			}
			
			//Adding them to the kills/deaths/killstreaks variables
			for(String playerName : plugin.inGame){
				plugin.playerKills.put(playerName, 0);
				plugin.playerDeaths.put(playerName, 0);
				plugin.killStreaks.put(playerName, 0);
			}
			
			if(type == GameType.DEATHMATCH){
				new GameEndTimer(plugin, 600).startGameEndTimer();
				sendGameMessage(ChatColor.RED + "Starting deathmatch game on the map '" + mapName + "'. 10 minutes remain.");
			}
			
			//Item Stuff
			for(String playerName : plugin.inGame){			
				Player player = Bukkit.getPlayer(playerName);
				restoreInventory(player);
			}
			
			//Scoreboard Stuff
			for(String playerName : plugin.inGame){
				Player player = Bukkit.getPlayer(playerName);
				ScoreboardTools.createScoreboardForPlayer(playerName + "-scoreboard", player);
				ScoreboardTools.update();
			}
		} else {
			Bukkit.broadcastMessage(plugin.chatTitle + ChatColor.RED + "Failed to start the game!");
			new GameCountdown(plugin, 120);
		}
	}
	
	//End a game
	public void endGame(){
		String winningTeamName;
		int winningTeamPoints;
		Color color;
		Color fadeColor;
		
		//Get the winning team
		if(plugin.redScore > plugin.blueScore){
			winningTeamName = ChatColor.RED + "Red" + ChatColor.RESET + "";
			winningTeamPoints = plugin.redScore;
			color = Color.RED;
			fadeColor = Color.RED;
		} else if(plugin.blueScore > plugin.redScore){
			winningTeamName = ChatColor.BLUE + "Blue" + ChatColor.RESET + "";
			winningTeamPoints = plugin.blueScore;
			color = Color.BLUE;
			fadeColor = Color.AQUA;
		} else {
			winningTeamName = ChatColor.LIGHT_PURPLE + "Draw" + ChatColor.RESET + "";
			winningTeamPoints = plugin.redScore;
			color = Color.FUCHSIA;
			fadeColor = Color.PURPLE;
		}
		
		//Teleport the players to the lobby
		for(String playerName : plugin.gameQueue){
			Player player = Bukkit.getPlayer(playerName);
			teleportToLobby(player);
		}
		
		//Tell the players who won
		sendGameMessage(ChatColor.GOLD + "The game has ended! Winning team: " + winningTeamName + ChatColor.GOLD + "(" + winningTeamPoints + " points)");
		
		//Shoot off a pretty little firework blast
		FireworkEffectPlayer fwPlayer = new FireworkEffectPlayer();
		FireworkEffect effect = FireworkEffect.builder().withColor(color).flicker(true).withFade(fadeColor).with(Type.BALL_LARGE).build();
		try{
			fwPlayer.playFirework(getLobbyLoc().getWorld(), getLobbyLoc(), effect);
		} catch(Exception e){
			e.printStackTrace();
			plugin.log("Failed to play firework effect! Error above!");
		}
	}
	
	//Add a player to the game
	public void addToGame(Player player){		
		if(plugin.gameStatus == GameStatus.INGAME){
			boolean randomBoolean = random.nextBoolean();
			int redSize = plugin.onRed.size();
			int blueSize = plugin.onBlue.size();
			
			if(redSize > blueSize){
				plugin.onBlue.add(player.getName());
			} else if(blueSize > redSize){
				plugin.onRed.add(player.getName());
			} else {
				if(randomBoolean){
					plugin.onRed.add(player.getName());
				} else {
					plugin.onBlue.add(player.getName());
				}
			}
		} else {
			if(!(plugin.gameQueue.contains(player.getName()))){
				plugin.gameQueue.add(player.getName());
			}
		}
	}
	
	//Remove a player from the game
	public void removeFromGame(Player player){
		if(plugin.onRed.contains(player.getName())){
			plugin.onRed.remove(player.getName());
		} else if(plugin.onBlue.contains(player.getName())){
			plugin.onBlue.remove(player.getName());
		}

		plugin.gameQueue.remove(player.getName());
		
		teleportToLobby(player);		
	}
	
	//Assign the players into teams
	public void chooseTeams(List<String> gamers){
		boolean randomBoolean = random.nextBoolean();
		
		plugin.inGame.clear();
		plugin.inGame = plugin.gameQueue;
		
		for(String playerName : gamers){
			if(plugin.onRed.size() > plugin.onBlue.size()){
				plugin.onBlue.add(playerName);
			} else if(plugin.onBlue.size() > plugin.onRed.size()){
				plugin.onRed.add(playerName);
			} else {
				if(randomBoolean){
					plugin.onRed.add(playerName);
				} else {
					plugin.onBlue.add(playerName);
				}
			}
		}
	}
	
	//Teleport a player to the lobby
	public void teleportToLobby(Player player){
		player.teleport(getLobbyLoc());
	}
	
	//Get the lobby location
	public Location getLobbyLoc(){
		World lobbyWorld = Bukkit.getWorlds().get(0);
		int lobbyX = lobbyWorld.getSpawnLocation().getBlockX();
		int lobbyY = lobbyWorld.getSpawnLocation().getBlockY();
		int lobbyZ = lobbyWorld.getSpawnLocation().getBlockZ();
		
		if(plugin.locationsFile.exists()){
			lobbyWorld = plugin.getServer().getWorld(plugin.locationsConfiguration.getString("lobby.world"));
			lobbyX = plugin.locationsConfiguration.getInt("lobby.x");
			lobbyY = plugin.locationsConfiguration.getInt("lobby.y");
			lobbyZ = plugin.locationsConfiguration.getInt("lobby.z");
		}
		
		Location lobby = new Location(lobbyWorld, lobbyX, lobbyY, lobbyZ);
		return lobby;
	}
	
	//Set the map team spawn for the specified map
	public void setMapTeamSpawn(TeamColor team, String map, Player setter){
		Location loc = setter.getLocation();
		String teamColor = team.toString().toLowerCase();
		
		plugin.locationsConfiguration.set("maps." + map + "." + teamColor + ".world", loc.getWorld().getName());
		plugin.locationsConfiguration.set("maps." + map + "." + teamColor + ".x", loc.getWorld().getName());
		plugin.locationsConfiguration.set("maps." + map + "." + teamColor + ".y", loc.getWorld().getName());
		plugin.locationsConfiguration.set("maps." + map + "." + teamColor + ".z", loc.getWorld().getName());
		plugin.saveLocationsFile();

		setter.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Successfully set the " + teamColor + " team spawn for the map '" + map + "'!");
	}

	//Create a map with the specified name
	public void createMap(String name, Player creator){
		if(plugin.locationsFile.exists()){
			plugin.locationsConfiguration.set("maps." + name, "");
			plugin.saveLocationsFile();
			creator.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Successfully created a map with the name '" + name +"'!");
			creator.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Use: " + ChatColor.AQUA + "/cod setspawn map " + name + " <red/blue>" + ChatColor.GREEN + " to set the team spawn.");
		} else {
			creator.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Failed to create the map! The locations.yml file does not exist!");
		}
	}
	
	//Delete the map with the specified name
	public void deleteMap(String name, Player deleter){
		if(plugin.locationsFile.exists()){
			if(plugin.locationsConfiguration.isConfigurationSection("maps." + name)){
				plugin.locationsConfiguration.set("maps." + name, null);
				plugin.saveLocationsFile();
				deleter.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Successfully deleted the map '" + name + "'!");
			} else {
				deleter.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Failed to delete the map! It does not exist!");
			}
		} else {
			deleter.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Failed to create the map! The locations.yml file does not exist!");
		}
	}
	
	//Set the lobby spawn location
	public void setLobbySpawn(Player player){
		if(plugin.locationsFile.exists()){
			Location loc = player.getLocation();
			plugin.locationsConfiguration.set("lobby.world", loc.getWorld().getName());
			plugin.locationsConfiguration.set("lobby.x", loc.getBlockX());
			plugin.locationsConfiguration.set("lobby.y", loc.getBlockY());
			plugin.locationsConfiguration.set("lobby.z", loc.getBlockZ());
			plugin.saveLocationsFile();
		} else {
			player.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Failed to set the lobby spawn! The locations.yml file does not exist!");
		}
	}
	
	//Give the specified player info about the plugin
	public void givePluginInfo(CommandSender sender){
		sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "-=- Minecraft Call of Duty -=-");
		sender.sendMessage(ChatColor.YELLOW + "-=- Developed by: " + ChatColor.RED + "Nauss (IGN) or chasechocolate (Bukkit)");
		sender.sendMessage(ChatColor.GREEN + "-=- Commands:");
		sender.sendMessage(ChatColor.AQUA + "/cod" + ChatColor.DARK_AQUA + " - Show brief info about plugin.");
		sender.sendMessage(ChatColor.AQUA + "/cod join" + ChatColor.DARK_AQUA + " - Join the game.");
		sender.sendMessage(ChatColor.AQUA + "/cod leave" + ChatColor.DARK_AQUA + " - Leave the game.");
		sender.sendMessage(ChatColor.AQUA + "/cod map create <name>" + ChatColor.DARK_AQUA + " - Create a COD map with the specified name.");
		sender.sendMessage(ChatColor.AQUA + "/cod map delete <name>" + ChatColor.DARK_AQUA + " - Delete a COD map with the specified name.");
		sender.sendMessage(ChatColor.AQUA + "/cod setspawn lobby" + ChatColor.DARK_AQUA + " - Set the lobby spawnpoint.");
		sender.sendMessage(ChatColor.AQUA + "/cod setspawn map <name> <red/blue>" + ChatColor.DARK_AQUA + " - Set the red or blue spawnpoint for the specified map.");
	}
	
	public ChatColor getChatColor(Player player){
		if(plugin.onRed.contains(player.getName())){
			return ChatColor.RED;
		} else if(plugin.onBlue.contains(player.getName())){
			return ChatColor.BLUE;
		} else {
			return ChatColor.WHITE;
		}
	}
	
	//Send a message to all the players in the game
	public void sendGameMessage(String msg){
		for(String playerName : plugin.inGame){
			Player player = Bukkit.getPlayer(playerName);
			player.sendMessage(msg);
		}
	}
	
	//Get the next GameType (random)
	public GameType getNextGameType(){
		int index = random.nextInt(GameType.values().length);
		GameType[] types = GameType.values();
		GameType type = types[index];
		return type;
	}
	
	//Get the TeamColor of the specific player
	public TeamColor getPlayerTeam(Player player){
		if(plugin.onRed.contains(player.getName())){
			return TeamColor.RED;
		} else if(plugin.onBlue.contains(player.getName())){
			return TeamColor.BLUE;
		} else {
			return null;
		}
	}
	
	//See if the game can start
	public boolean canStart(){
		if(getAllMaps().size() == 0){
			return false;
		} else if(plugin.gameQueue.size() < 3){
			return false;
		} else {
			return true;
		}
	}
	
	//Setup a player for a game (heal, feed, set items, set armor)
	public void restoreInventory(Player player){
		PlayerInventory inv = player.getInventory();
		
		if(plugin.gameType == GameType.DEATHMATCH){
			if(getPlayerTeam(player) == TeamColor.RED){
				inv.clear();
				player.setHealth(20);
				player.setFoodLevel(20);
				
				inv.setHelmet(Localization.LEATHER_HELMET_RED);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_RED);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_RED);
				inv.setBoots(Localization.LEATHER_BOOTS_RED);
				
				inv.addItem(Localization.IRON_SWORD);
			} else if(getPlayerTeam(player) == TeamColor.BLUE){
				inv.clear();
				player.setHealth(20);
				player.setFoodLevel(17);
				
				inv.setHelmet(Localization.LEATHER_HELMET_BLUE);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_BLUE);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_BLUE);
				inv.setBoots(Localization.LEATHER_BOOTS_BLUE);
				
				inv.addItem(Localization.IRON_SWORD);
				inv.addItem(Localization.WOOD_HOE);
				inv.addItem(Localization.IRON_HOE);
				inv.setItem(9, Localization.SLIME_BALL);
			}
		}
	}
}