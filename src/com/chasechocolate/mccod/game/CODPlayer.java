package com.chasechocolate.mccod.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.utils.LocationUtils;
import com.chasechocolate.mccod.utils.PlayerStats;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class CODPlayer { //Thanks to chaseoes for inspiring me to add this class! Some method/variable ideas I don't take credit for! :)
	private Player player;
	
	private Map map;
	private TeamColor team;
	
	private int kills;
	private int totalKills;
	private int deaths;
	private int totalDeaths;
	private int currentKillstreak;
	
	private long timeEnteredGame;
	
	private boolean inGame;
	private boolean invincible;
	
	private Set<Integer> killstreaks;
	
	private PlayerStats stats;
	
	private Gun gun;
	
	public CODPlayer(Player player){
		this.player = player;
		this.map = null;
		this.team = null;
		this.kills = 0;
		this.totalKills = 0;
		this.deaths = 0;
		this.totalDeaths = 0;
		this.currentKillstreak = 0;
		this.timeEnteredGame = System.currentTimeMillis();
		this.inGame = false;
		this.invincible = false;
		this.killstreaks = new HashSet<Integer>();
		this.stats = new PlayerStats(player);
	}
	
	public Arena getArena(){
		return ArenaUtils.getPlayerArena(player);
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public String getName(){
		return player.getName();
	}
	
	public boolean isPlayingGame(){
		return inGame;
	}
	
	public void setPlayingGame(boolean playing){
		this.inGame = playing;
	}
	
	public Map getCurrentMap(){
		return map;
	}
	
	public void setCurrentMap(Map map){
		this.map = map;
	}
	
	public TeamColor getTeam(){
		return this.team;
	}
	
	public void setTeam(TeamColor team){
		this.team = team;
	}
	
	public int getKills(){
		return kills;
	}
	
	public void setKills(int kills){
		this.kills = kills;
	}
	
	public int getTotalKills(){
		return this.totalKills;
	}
	
	public void setTotalKills(int totalKills){
		this.totalKills = totalKills;
	}
	
	public int getDeaths(){
		return this.deaths;
	}
	
	public void setDeaths(int deaths){
		this.deaths = deaths;
	}
	
	public int getTotalDeaths(){
		return this.totalDeaths;
	}
	
	public void setTotalDeaths(int totalDeaths){
		this.totalDeaths = totalDeaths;
	}
	
	public int getCurrentKillstreak(){
		return this.currentKillstreak;
	}
	
	public void setCurrentKillstreak(int killstreak){
		this.currentKillstreak = killstreak;
	}
	
	public PlayerStats getStats(){
		return this.stats;
	}
	
	public void quitCurrentGame(){
		getArena().removePlayer(player);
		PlayerUtils.wipe(player);
	}
	
	public Gun getGun(){
		return this.gun;
	}
	
	public void setGun(Gun gun){
		this.gun = gun;
	}
	
	public boolean isInvincible(){
		return this.invincible;
	}
	
	public void setInvincible(boolean invincible){
		this.invincible = invincible;
	}
	
	public long getTimeEnteredGame(){
		return this.timeEnteredGame;
	}
	
	public void setTimeEnteredGame(){
		this.timeEnteredGame = System.currentTimeMillis();
	}
	
	public int getTotalTimeInGame(){
		return (int) ((System.currentTimeMillis() - timeEnteredGame) / 1000);
	}
	
	public int getHighestKillstreak(){
		if(killstreaks.isEmpty()){
			return 0;
		} else {
			return Collections.max(killstreaks);
		}
	}
	
	public void addKillstreak(int killstreak){
		killstreaks.add(killstreak);
	}
	
	public void teleportToLobby(){
		getPlayer().teleport(LocationUtils.getLobbyLoc());
	}
}