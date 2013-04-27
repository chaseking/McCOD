package com.chasechocolate.mccod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.chasechocolate.mccod.cmds.CommandManager;
import com.chasechocolate.mccod.game.GameManager;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.Gun;
import com.chasechocolate.mccod.game.map.MapUtils;
import com.chasechocolate.mccod.listeners.EntityDamageListener;
import com.chasechocolate.mccod.listeners.HeartsHungerListener;
import com.chasechocolate.mccod.listeners.InventoryClickListener;
import com.chasechocolate.mccod.listeners.ItemConsumeListener;
import com.chasechocolate.mccod.listeners.ItemPickupListener;
import com.chasechocolate.mccod.listeners.NoCommands;
import com.chasechocolate.mccod.listeners.PlayerDeathListener;
import com.chasechocolate.mccod.listeners.PlayerInteractListener;
import com.chasechocolate.mccod.listeners.PlayerItemDropListener;
import com.chasechocolate.mccod.listeners.PlayerJoinLeaveListener;
import com.chasechocolate.mccod.listeners.PlayerRespawnListener;
import com.chasechocolate.mccod.listeners.TagListener;
import com.chasechocolate.mccod.mysql.SQLUtils;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;
import com.chasechocolate.mccod.timers.GameCountdown;
import com.chasechocolate.mccod.timers.GameEndTimer;
import com.chasechocolate.mccod.utils.Localization;
import com.chasechocolate.mccod.utils.Metrics;
import com.chasechocolate.mccod.utils.PlayerStatistics;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class McCOD extends JavaPlugin {
	public File configFile = new File(this.getDataFolder(), "config.yml");
	public File locationsFile = new File(this.getDataFolder(), "locations.yml");
	public File gunsFile = new File(this.getDataFolder(), "guns.yml");
	
	public FileConfiguration locationsConfiguration;
	public FileConfiguration gunsConfiguration;
	
	public final String chatTitle = ChatColor.GRAY + "[" + ChatColor.GREEN + "McCOD" + ChatColor.GRAY + "]" + ChatColor.RESET + " ";
	public final String noPerms = this.chatTitle + ChatColor.RED + "You don't have permission!";
	public String currentMap;
	
	public List<String> gameQueue = new ArrayList<String>();
	public List<String> inGame = new ArrayList<String>();
	public List<String> onRed = new ArrayList<String>();
	public List<String> onBlue = new ArrayList<String>();
	public List<Location> carePackages = new ArrayList<Location>();
	public List<UUID> droppedItems = new ArrayList<UUID>();
	public List<Gun> guns = new ArrayList<Gun>();
	
	public HashMap<String, Integer> playerKills = new HashMap<String, Integer>();
	public HashMap<String, Integer> playerDeaths = new HashMap<String, Integer>();
	public HashMap<String, Integer> killStreaks = new HashMap<String, Integer>();
	
	public int redScore = 0;
	public int blueScore = 0;
	
	public GameManager gameManager = new GameManager(this);
	public GameStatus gameStatus;
	public GameEndTimer gameEndTimer;
	public GameType gameType;
	
	public void log(String msg){
		this.getLogger().info(msg);
	}
	
	@Override
	public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
		
		if(pm.getPlugin("TagAPI") != null){
			log("Successfully hooked into TagAPI");
		} else {
			this.getLogger().log(Level.WARNING, "Found no TagAPI!");
			this.getLogger().log(Level.WARNING, "Shutting down McCOD...");
			pm.disablePlugin(this);
			return;
		}
		
		if(!(configFile.exists())){
			log("Found no config.yml! Creating one for you...");
			this.saveDefaultConfig();
			log("Successfully created config.yml!");
		}
		
		if(!(locationsFile.exists())){
			log("Found no locations.yml! Creating one for you...");
			this.saveResource("locations.yml", false);
			log("Successfully created locations.yml!");
		}
		
		if(!(gunsFile.exists())){
			log("Found no guns.yml! Creating one for you...");
			this.saveResource("guns.yml", false);
			log("Successfully created guns.yml");
		}
		
		if(this.getConfig().getBoolean("metrics.enabled")){
			log("Attempting to setup Metrics...");
			
			try{
				Metrics metrics = new Metrics(this);
				metrics.start();
				log("Successfully setup Metics!");
			} catch(IOException e){
				log("Failed to setup Metrics!");
			}
		}
		
		//Initialization
		loadGuns();
		
		PlayerStatistics.init(this);
		Localization.init();
		ScoreboardTools.init(this);
		SQLUtils.setup(this);
		
		new MapUtils(locationsFile, locationsConfiguration);
		new PlayerUtils(this);
		
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new EntityDamageListener(this), this);
		pm.registerEvents(new NoCommands(this), this);
		pm.registerEvents(new PlayerDeathListener(this), this);
		pm.registerEvents(new PlayerItemDropListener(this), this);
		pm.registerEvents(new PlayerJoinLeaveListener(this), this);
		pm.registerEvents(new TagListener(this), this);
		pm.registerEvents(new PlayerRespawnListener(this), this);
		pm.registerEvents(new ItemPickupListener(this), this);
		pm.registerEvents(new InventoryClickListener(this), this);
		pm.registerEvents(new HeartsHungerListener(this), this);
		pm.registerEvents(new ItemConsumeListener(this), this);
				
		this.getCommand("cod").setExecutor(new CommandManager(this));
		this.getCommand("callofduty").setExecutor(new CommandManager(this));
		
		this.currentMap = gameManager.getRandomMap();
		
		new GameCountdown(this, 120).startGameCountdown();
		
		log("Enabled!");
	}
	
	@Override
	public void onDisable(){
		for(String playerName : this.inGame){
			Player player = Bukkit.getPlayer(playerName);
			gameManager.teleportToLobby(player);
			player.sendMessage(ChatColor.RED + "You have been teleported because the server is reloading or stopping!");
		}
		
		log("Disabled!");
	}
	
	public void loadConfig(){
		//Nothing yet
	}
	
	public void loadGuns(){
		if(this.gunsFile.exists()){
			for(String gunName : gunsConfiguration.getKeys(false)){
				int damage;
				int item;
				
				if(gunsConfiguration.isInt(gunName + ".damage")){
					damage = gunsConfiguration.getInt(gunName + ".damage");
				} else {
					damage = 6;
				}
				
				if(gunsConfiguration.isInt(gunName + ".item")){
					item = gunsConfiguration.getInt(gunName + ".item");
				} else {
					item = 292;
				}
				
				Gun gun = new Gun(gunName, item, damage);
				
				this.guns.add(gun);
			}
		}
	}
	
	public void saveLocationsFile(){
		try{
			locationsConfiguration.save(locationsFile);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setLobbySpawn(Location loc){
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		
		locationsConfiguration.set("lobby.world", world);
		locationsConfiguration.set("lobby.x", x);
		locationsConfiguration.set("lobby.y", y);
		locationsConfiguration.set("lobby.z", z);
		locationsConfiguration.set("lobby.yaw", yaw);
		locationsConfiguration.set("lobby.pitch", pitch);
		
		saveLocationsFile();
	}
}