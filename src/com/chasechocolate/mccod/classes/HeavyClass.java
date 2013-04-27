package com.chasechocolate.mccod.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.Localization;

public class HeavyClass extends CODClass {
	private String name = "Heavy";
	private String cmd = "heavy";
	
	private ItemStack dhelmet = new ItemStack(Material.DIAMOND_HELMET);
	private ItemStack dchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
	private ItemStack dleggings = new ItemStack(Material.DIAMOND_LEGGINGS);
	private ItemStack dboots = new ItemStack(Material.DIAMOND_BOOTS);
	private ItemStack dsword = new ItemStack(Material.DIAMOND_SWORD);
	
	public HeavyClass(McCOD plugin){
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
		PlayerInventory inv = player.getInventory();
		
		inv.clear();
		
		inv.setHelmet(dhelmet);
		inv.setChestplate(dchestplate);
		inv.setLeggings(dleggings);
		inv.setBoots(dboots);
		
		inv.addItem(dsword);
		
		for(int i = 0; i < Localization.HEAVY_STEAK_AMOUNT; i++){
			inv.addItem(Localization.STEAK);
		}
	}
}