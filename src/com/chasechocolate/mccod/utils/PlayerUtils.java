package com.chasechocolate.mccod.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.effects.FireworkEffectPlayer;
import com.chasechocolate.mccod.game.CODPlayer;
import com.chasechocolate.mccod.game.GameType;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.TeamColor;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.gun.Gun;
import com.chasechocolate.mccod.game.gun.GunUtils;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.scoreboards.ScoreboardTools;

public class PlayerUtils {
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
		
		for(Arena arena : ArenaUtils.getAllArenas()){
			for(Player player : arena.getAllPlayers()){
				allPlayers.add(player);
			}
		}
		
		return allPlayers;
	}
	
	public static void shootGun(Player player){
		if(GameUtils.isInGame(player)){
			if(GunUtils.getPlayerGun(player) != null){
				PlayerInventory inv = player.getInventory();
				Gun gun = GunUtils.getPlayerGun(player);
				Vector velocity = player.getLocation().getDirection().multiply(3);
				ItemStack ammo = new ItemStack(Material.ARROW, 1);
				
				if(inv.containsAtLeast(ammo, 1)){
					Snowball snowball = player.launchProjectile(Snowball.class);
					UUID id = snowball.getUniqueId();
					
					snowball.setVelocity(velocity);
					GunUtils.shotBullets.put(id, gun);
					player.getInventory().remove(ammo);
				}
			}
		}
	}
	
	public static void tossGrenade(final Player player){
		if(GameUtils.isInGame(player)){
			PlayerInventory inv = player.getInventory();
			Location loc = player.getLocation();
			ItemStack grenade = new ItemStack(Material.SLIME_BALL, 1);
			
			if(inv.contains(Material.SLIME_BALL)){
				final Item drop = player.getWorld().dropItem(loc.add(0.0D, 1.0D, 0.0D), grenade);
				plugin.droppedItems.add(drop.getUniqueId());
				Vector currentVelocity = player.getLocation().getDirection();
				drop.setVelocity(currentVelocity.multiply(1.15D));
				inv.removeItem(grenade);
				
				new BukkitRunnable(){
					@Override
					public void run(){
						FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).withColor(Color.ORANGE).withFade(Color.YELLOW).with(Type.BALL_LARGE).build();
						FireworkEffectPlayer fireworkPlayer = new FireworkEffectPlayer();
						
						try{
							fireworkPlayer.playFirework(drop.getWorld(), drop.getLocation(), effect);
						} catch(Exception e){
							drop.getWorld().createExplosion(drop.getLocation(), 0.0F);
						}
						
						for(Entity nearbyEntity : drop.getNearbyEntities(5.0D, 5.0D, 5.0D)){
							if(nearbyEntity instanceof Player){
								Player nearbyPlayer = (Player) nearbyEntity;
								if(sameArena(player, nearbyPlayer)){
									if(!(sameTeam(player, nearbyPlayer))){											
										nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 130, 1));
										nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 130, 1));
									}
								}
							}
						}
						
						drop.remove();
					}
				}.runTaskLater(plugin, 100L);
			}
		}
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
				
				inv.setItem(8, Localization.RED_WOOL);
			} else if(PlayerUtils.getTeam(player) == TeamColor.BLUE){
				inv.setHelmet(Localization.LEATHER_HELMET_BLUE);
				inv.setChestplate(Localization.LEATHER_CHESTPLATE_BLUE);
				inv.setLeggings(Localization.LEATHER_LEGGINGS_BLUE);
				inv.setBoots(Localization.LEATHER_BOOTS_BLUE);
				
				inv.setItem(8, Localization.BLUE_WOOL);
			}
			
			inv.addItem(Localization.IRON_SWORD);
			
			Gun gun = GunUtils.getPlayerGun(player);
			
			if(gun != null){				
				inv.addItem(gun.toItemStack());
				int ammoSlot = 9;
				
				for(ItemStack ammo : gun.ammoToItemStack()){
					inv.setItem(ammoSlot, ammo);
					ammoSlot += 1;
				}
			}
			
			inv.setItem(7, Localization.SLIME_BALL);
		}
	}
	
	public static void respawn(Player player){
		if(GameUtils.isInGame(player)){
			Arena arena = ArenaUtils.getPlayerArena(player);
			Map map = arena.getMap();
			TeamColor team = getTeam(player);
			Location teamSpawn = map.getTeamSpawn(team);
			
			player.teleport(teamSpawn);
			restoreInventory(player);
			ScoreboardTools.update();
		}
	}
	
	public static TeamColor getTeam(Player player){
		if(GameUtils.isInGame(player)){
			if(ArenaUtils.getPlayerArena(player).getPlayersOnTeam(TeamColor.RED).contains(player)){
				return TeamColor.RED;
			} else if(ArenaUtils.getPlayerArena(player).getPlayersOnTeam(TeamColor.BLUE).contains(player)){
				return TeamColor.BLUE;
			} else {
				return null;
			}
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