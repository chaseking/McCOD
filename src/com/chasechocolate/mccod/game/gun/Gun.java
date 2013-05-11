package com.chasechocolate.mccod.game.gun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gun {
	private String name;
	private String permission;
	
	private int item;
	private int damage;
	private int ammo;
	
	public Gun(String name, int item, int damage, int ammo){
		this.name = name;
		this.permission = "mccod.gun." + name;
		this.item = item;
		this.damage = damage;
		this.ammo = ammo;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getPermission(){
		return this.permission;
	}
	
	public int getItem(){
		return this.item;
	}
	
	public void setItem(int item){
		this.item = item;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public void setDamage(int damage){
		this.damage = damage;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public void setAmmo(int ammo){
		this.ammo = ammo;
	}
	
	public ItemStack toItemStack(){
		ItemStack gun = new ItemStack(item, 1);
		ItemMeta meta = gun.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BOLD + "" + ChatColor.AQUA + "Damage: " + ChatColor.RED + damage);
		lore.add(ChatColor.BOLD + "" + ChatColor.AQUA + "Ammo: " + ChatColor.RED + ammo);
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + name);
		meta.setLore(lore);
		gun.setItemMeta(meta);
		
		return gun;
	}
	
	public List<ItemStack> ammoToItemStack(){
		List<ItemStack> ammo = new ArrayList<ItemStack>();
		
		int maxStackSize = Material.ARROW.getMaxStackSize();
		int stacks = this.ammo / maxStackSize;
		int leftOver = this.ammo % maxStackSize;
		
		for(int i = 0; i < stacks; i++){
			ammo.add(new ItemStack(Material.ARROW, maxStackSize));
		}
		
		ammo.add(new ItemStack(Material.ARROW, leftOver));
		
		return ammo;
	}
}
