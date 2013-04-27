package com.chasechocolate.mccod.classes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.Localization;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class ArcherClass extends CODClass {
	private String name = "Archer";
	private String cmd = "archer";
	
	private ItemStack chelmet = new ItemStack(Material.CHAINMAIL_HELMET);
	private ItemStack cchestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
	private ItemStack cleggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
	private ItemStack cboots = new ItemStack(Material.CHAINMAIL_BOOTS);
	private ItemStack ssword = new ItemStack(Material.STONE_SWORD);
	private ItemStack bow = new ItemStack(Material.BOW);
	private ItemStack arrow = new ItemStack(Material.ARROW, 64);
	
	public ArcherClass(McCOD plugin){
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
		
		PlayerUtils.wipe(player);
		
		inv.setHelmet(chelmet);
		inv.setChestplate(cchestplate);
		inv.setLeggings(cleggings);
		inv.setBoots(cboots);
		
		inv.addItem(ssword);
		
		bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
		inv.addItem(bow);
		
		inv.addItem(arrow);
		inv.addItem(arrow);
		
		for(int i = 0; i < Localization.HEAVY_STEAK_AMOUNT; i++){
			inv.addItem(Localization.STEAK);
		}
	}
}
