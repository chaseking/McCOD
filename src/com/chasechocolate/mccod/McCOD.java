package com.chasechocolate.mccod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.chasechocolate.mccod.cmds.CommandManager;
import com.chasechocolate.mccod.game.GameManager;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.Gun;
import com.chasechocolate.mccod.game.map.MapUtils;
import com.chasechocolate.mccod.listeners.CODDeathListener;
import com.chasechocolate.mccod.listeners.EntityDamageListener;
import com.chasechocolate.mccod.listeners.HeartsHungerListener;
import com.chasechocolate.mccod.listeners.InventoryClickListener;
import com.chasechocolate.mccod.listeners.ItemConsumeListener;
import com.chasechocolate.mccod.listeners.ItemPickupListener;
import com.chasechocolate.mccod.listeners.NoCommands;
import com.chasechocolate.mccod.listeners.PlayerInteractListener;
import com.chasechocolate.mccod.listeners.PlayerItemDropListener;
import com.chasechocolate.mccod.listeners.PlayerJoinLeaveListener;
import com.chasechocolate.mccod.listeners.TagListener;
import com.chasechocolate.mccod.mysql.SQLUtils;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;
import com.chasechocolate.mccod.timers.GameEndTimer;
import com.chasechocolate.mccod.utils.Config;
import com.chasechocolate.mccod.utils.Localization;
import com.chasechocolate.mccod.utils.Metrics;
import com.chasechocolate.mccod.utils.PlayerUtils;
import com.chasechocolate.mccod.utils.StatUtils;

public class McCOD extends JavaPlugin {	
	public final String chatTitle = ChatColor.GRAY + "[" + ChatColor.GREEN + "McCOD" + ChatColor.GRAY + "]" + ChatColor.RESET + " ";
	public final String noPerms = this.chatTitle + ChatColor.RED + "You don't have permission!";
	public String currentMap;
	
	public long statsSendDelay;
	
	public List<String> gameQueue = new ArrayList<String>();
	public List<String> inGame = new ArrayList<String>();
	public List<String> onRed = new ArrayList<String>();
	public List<String> onBlue = new ArrayList<String>();
	public List<UUID> droppedItems = new ArrayList<UUID>();
	public List<Gun> guns = new ArrayList<Gun>();
	
	public HashMap<String, Integer> playerKills = new HashMap<String, Integer>();
	public HashMap<String, Integer> playerDeaths = new HashMap<String, Integer>();
	public HashMap<String, Integer> killStreaks = new HashMap<String, Integer>();
	
	public SortedMap<Integer, String> commandHelp = new TreeMap<Integer, String>();
	
	public GameManager gameManager = new GameManager(this);
	public GameStatus gameStatus;
	public GameEndTimer gameEndTimer;
	public GameType gameType;
	
	private static McCOD instance = new McCOD();
	
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
		
		//Configuration files
		new Config(this);
		Config.createAllFiles();
		
		if(Config.getConfig().getBoolean("metrics.enabled")){
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
		Localization.init();
		ScoreboardTools.init(this);
		SQLUtils.setup(this);
		
		new MapUtils();
		new PlayerUtils(this);
		
		commandHelp.put(1, "/cod join <arena> - Join an arena with the specified name.");
		//TODO more help messages
		
		//Loading config
		this.statsSendDelay = Config.getConfig().getLong("stats.push-delay");
		
		//Registering events
		pm.registerEvents(new CODDeathListener(this), this);
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new EntityDamageListener(this), this);
		pm.registerEvents(new NoCommands(this), this);
		pm.registerEvents(new PlayerItemDropListener(this), this);
		pm.registerEvents(new PlayerJoinLeaveListener(this), this);
		pm.registerEvents(new TagListener(this), this);
		pm.registerEvents(new ItemPickupListener(this), this);
		pm.registerEvents(new InventoryClickListener(this), this);
		pm.registerEvents(new HeartsHungerListener(this), this);
		pm.registerEvents(new ItemConsumeListener(this), this);
		
		//Registering commands
		this.getCommand("cod").setExecutor(new CommandManager(this));
		this.getCommand("callofduty").setExecutor(new CommandManager(this));
		
		//Start timer to automatically send statistics to database
		StatUtils.startStatSender(this, statsSendDelay);
		
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
	
	public static McCOD getInstance(){
		return instance;
	}
}