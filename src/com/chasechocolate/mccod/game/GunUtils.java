package com.chasechocolate.mccod.game;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunUtils {
	public static ItemStack getItemStackFromGun(Gun gun){
		ItemStack gunItem = gun.toItemStack();
		ItemMeta meta = gunItem.getItemMeta();
		meta.setDisplayName(gun.getName());
		gunItem.setItemMeta(meta);
		
		return gunItem;
	}
}
