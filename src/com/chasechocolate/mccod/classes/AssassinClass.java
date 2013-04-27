package com.chasechocolate.mccod.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.Localization;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class AssassinClass extends CODClass {
	private String name = "Assassin";
	private String cmd = "assassin";
	
	private ItemStack gboots = new ItemStack(Material.GOLD_BOOTS);
	private ItemStack isword = new ItemStack(Material.IRON_SWORD);
	private ItemStack redstone = new ItemStack(Material.REDSTONE, 2);
	private ItemStack sugar = new ItemStack(Material.SUGAR, 3);
	
	public AssassinClass(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public String getCommand(){
		return this.cmd;
	}
	
	@Override
	public void apply(Player player){
		final PlayerInventory inv = player.getInventory();
		
		PlayerUtils.wipe(player);
		
		inv.setBoots(gboots);
		
		inv.addItem(isword);
		inv.addItem(redstone);
		inv.addItem(sugar);
		
		for(int i = 0; i < Localization.HEAVY_STEAK_AMOUNT; i++){
			inv.addItem(Localization.STEAK);
		}
		
		new BukkitRunnable(){
			@Override
			public void run(){
				for(ItemStack item : inv.getContents()){
					int amount = item.getAmount();
					
					if(item.getType() == Material.SUGAR){
						if(amount > 3){
							inv.addItem(new ItemStack(Material.SUGAR));
						}
					} else if(item.getType() == Material.REDSTONE){
						if(amount > 2){
							inv.addItem(new ItemStack(Material.REDSTONE));
						}
					}
				}
			}
		}.runTaskTimer(plugin, 600L, 600L);
	}
}
