package com.chasechocolate.mccod.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;
import com.chasechocolate.mccod.game.gun.Gun;
import com.chasechocolate.mccod.game.gun.GunUtils;
import com.chasechocolate.mccod.utils.Localization;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class PlayerInteractListener implements Listener {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public PlayerInteractListener(McCOD plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack hand = player.getItemInHand();
		
		if(GameUtils.isInGame(player)){
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
				Block block = event.getClickedBlock();
				if(block.getState() instanceof Sign){
					Sign sign = (Sign) block.getState();
					if(sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[McCOD]")){
						if(sign.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "Join")){
							if(ArenaUtils.isArena(ChatColor.stripColor(sign.getLine(2)))){
								Arena arena = ArenaUtils.getArena(sign.getLine(2));
								if(ArenaUtils.getAllArenas().contains(arena)){
									arena.addPlayer(player);
								} else {
									player.sendMessage(ChatColor.RED + "The arena '" + sign.getLine(2) + "' is not running!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "The arena '" + sign.getLine(2) + "' does not exist!");
							}
						}
					}
				}
			}
			
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
				if(GunUtils.getPlayerGun(player) != null){				
					Gun gun = GunUtils.getPlayerGun(player);
					ItemStack gunItem = gun.toItemStack();
					Material type = gunItem.getType();
					
					if(hand.getType() == type && hand.getItemMeta().equals(gunItem.getItemMeta())){
						PlayerUtils.shootGun(player);
					}
				}
				
				if(hand.getType() == Material.SLIME_BALL){
					PlayerUtils.tossGrenade(player);
				}
				
				if(hand.equals(Localization.RED_WOOL) || hand.equals(Localization.BLUE_WOOL)){
					GunUtils.getGunMenu().open(player);
				}
			}
		}
	}
}