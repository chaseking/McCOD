package com.chasechocolate.mccod.game;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gun {
	private String name;
	private int item;
	private int damage;
	
	public Gun(String name, int item, int damage){
		this.name = name;
		this.item = item;
		this.damage = damage;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
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
	
	public ItemStack toItemStack(){
		ItemStack gun = new ItemStack(item, 1);
		ItemMeta meta = gun.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + name);
		gun.setItemMeta(meta);
		
		return gun;
	}
}
