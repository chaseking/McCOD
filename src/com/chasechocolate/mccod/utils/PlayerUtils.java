package com.chasechocolate.mccod.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.CODPlayer;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.Gun;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.map.Map;

public class PlayerUtils {
	@SuppressWarnings("unused")
	private static McCOD plugin;
	
	private static HashMap<Arena, CODPlayer> gamePlayersInArenas = new HashMap<Arena, CODPlayer>();
	
	@SuppressWarnings("static-access")
	public PlayerUtils(McCOD plugin){
		this.plugin = plugin;
	}
	
	public static void wipe(Player player){
		PlayerInventory inv = player.getInventory();
		
		inv.clear();
		inv.setArmorContents(null);
		player.setExp(0);
		player.setLevel(0);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setRemainingAir(player.getMaximumAir());
		player.setFireTicks(0);
		
		for(PotionEffect pe : player.getActivePotionEffects()){
			player.removePotionEffect(pe.getType());
		}
	}
	
	public static CODPlayer getCODPlayer(Player player){
		for(CODPlayer codPlayer : gamePlayersInArenas.values()){
			if(player.getName().equals(codPlayer.getName())){
				return codPlayer;
			}
		}
		
		return null;
	}

	public static List<Player> getAllActivePlayers(){
		List<Player> allPlayers = new ArrayList<Player>();
		
		for(Arena arena : ArenaUtils.getCurrentArenas()){
			for(Player player : arena.getAllPlayers()){
				allPlayers.add(player);
			}
		}
		
		return allPlayers;
	}
	
	public static void restoreInventory(Player player){
		wipe(player);
		
		PlayerInventory inv = player.getInventory();
		
		if(ArenaUtils.getPlayerArena(player).getGameType() == GameType.DEATHMATCH){
			if(PlayerUtils.getTeam(player) == TeamColor.RED){
				inv.setHelmet(Localization.LEATHER_HELMET_RED);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_RED);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_RED);
				inv.setBoots(Localization.LEATHER_BOOTS_RED);
			} else if(PlayerUtils.getTeam(player) == TeamColor.BLUE){
				inv.setHelmet(Localization.LEATHER_HELMET_BLUE);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_BLUE);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_BLUE);
				inv.setBoots(Localization.LEATHER_BOOTS_BLUE);
			}
			
			inv.addItem(Localization.IRON_SWORD);
			
			Gun gun = GunUtils.getPlayerGun(player);
			
			if(gun != null){				
				inv.addItem(gun.toItemStack());
			}
			
			inv.setItem(9, Localization.SLIME_BALL);
		}
	}
	
	public static void respawn(Player player){
		Arena arena = ArenaUtils.getPlayerArena(player);
		Map map = arena.getMap();
		TeamColor team = getTeam(player);
		Location teamSpawn = map.getTeamSpawn(team);
		
		player.teleport(teamSpawn);
		restoreInventory(player);
	}
	
	public static TeamColor getTeam(Player player){
		if(ArenaUtils.getPlayerArena(player).getPlayersOnTeam(TeamColor.RED).contains(player)){
			return TeamColor.RED;
		} else if(ArenaUtils.getPlayerArena(player).getPlayersOnTeam(TeamColor.BLUE).contains(player)){
			return TeamColor.BLUE;
		} else {
			return null;
		}
	}
	
	public static boolean sameTeam(Player player1, Player player2){
		boolean same = getTeam(player1) == getTeam(player2);
		return same;
	}
	
	public static boolean sameArena(Player player1, Player player2){
		boolean same = ArenaUtils.getPlayerArena(player1).equals(ArenaUtils.getPlayerArena(player2));
		return same;
	}
}