package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.chasechocolate.mccod.McCOD;

public class PlayerInteractListener implements Listener {
	private McCOD plugin;
	public PlayerInteractListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		PlayerInventory playerInv = player.getInventory();
		if(plugin.inGame.contains(player.getName())){
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(event.getClickedBlock().getState() instanceof Sign){
					Sign sign = (Sign) event.getClickedBlock().getState();
					if(sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[McCOD]")){
						if(sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "Join")){
							plugin.gameManager.addToGame(player);
							return;
						}
					}
				}
			}
			
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(player.getItemInHand().getType() == Material.IRON_HOE){
					if(playerInv.contains(new ItemStack(Material.SNOW_BALL, 1))){
						player.launchProjectile(Snowball.class);
						playerInv.remove(new ItemStack(Material.SNOW_BALL, 1));
					}
				} else if(player.getItemInHand().getType() == Material.WOOD_HOE){
					if(playerInv.contains(new ItemStack(Material.SNOW_BALL, 1))){
						player.launchProjectile(Snowball.class);
						playerInv.remove(new ItemStack(Material.SNOW_BALL, 1));
					}
				} else if(player.getItemInHand().getType() == Material.SLIME_BALL){
					if(playerInv.contains(new ItemStack(Material.SLIME_BALL, 1))){
						final Item drop = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.SLIME_BALL, 1));
						plugin.droppedItems.add(drop.getUniqueId());
						Vector currentVelocity = player.getLocation().getDirection();
						drop.setVelocity(currentVelocity.multiply(1.5D));
						
						new BukkitRunnable(){
							@Override
							public void run(){
								drop.getWorld().createExplosion(drop.getLocation(), 0.0F);
								drop.remove();
								for(Entity nearbyEntity : drop.getNearbyEntities(5.0D, 5.0D, 5.0D)){
									if(nearbyEntity instanceof Player){
										Player nearbyPlayer = (Player) nearbyEntity;
										nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 130, 1));
										nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 130, 1));
									}
								}
							}
						}.runTaskLater(plugin, 100L);
					}
				}
			}
		}
	}
}