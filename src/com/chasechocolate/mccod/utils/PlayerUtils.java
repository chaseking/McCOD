package com.chasechocolate.mccod.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.CODPlayer;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.Gun;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;

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
	
	public static void restoreInventory(Player player){
		CODPlayer codPlayer = PlayerUtils.getCODPlayer(player);
		wipe(player);
		PlayerInventory inv = player.getInventory();
		
		if(codPlayer.getArena().getGameType() == GameType.DEATHMATCH){
			if(codPlayer.getTeam() == TeamColor.RED){
				inv.setHelmet(Localization.LEATHER_HELMET_RED);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_RED);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_RED);
				inv.setBoots(Localization.LEATHER_BOOTS_RED);
			} else if(codPlayer.getTeam() == TeamColor.BLUE){
				inv.setHelmet(Localization.LEATHER_HELMET_BLUE);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_BLUE);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_BLUE);
				inv.setBoots(Localization.LEATHER_BOOTS_BLUE);
			}
			
			inv.addItem(Localization.IRON_SWORD);
			
			Gun gun = codPlayer.getGun();
			if(gun != null){				
				inv.addItem(GunUtils.getItemStackFromGun(gun));
			}
			
			inv.setItem(9, Localization.SLIME_BALL);
		}
	}
}