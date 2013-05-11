package com.chasechocolate.mccod.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.permissions.Permission;

public class Localization {
	public static ItemStack LEATHER_HELMET_RED = new ItemStack(Material.LEATHER_HELMET);
	public static ItemStack LEATHER_CHESTPLATE_RED = new ItemStack(Material.LEATHER_CHESTPLATE);
	public static ItemStack LEATHER_LEGGINGS_RED = new ItemStack(Material.LEATHER_LEGGINGS);
	public static ItemStack LEATHER_BOOTS_RED = new ItemStack(Material.LEATHER_BOOTS);
	
	public static ItemStack LEATHER_HELMET_BLUE = new ItemStack(Material.LEATHER_HELMET);
	public static ItemStack LEATHER_CHESTPLATE_BLUE = new ItemStack(Material.LEATHER_CHESTPLATE);
	public static ItemStack LEATHER_LEGGINGS_BLUE = new ItemStack(Material.LEATHER_LEGGINGS);
	public static ItemStack LEATHER_BOOTS_BLUE = new ItemStack(Material.LEATHER_BOOTS);
	
	public static ItemStack IRON_SWORD = new ItemStack(Material.IRON_SWORD, 1);
	
	public static ItemStack WOOD_HOE = new ItemStack(Material.WOOD_HOE, 1);
	public static ItemStack IRON_HOE = new ItemStack(Material.IRON_HOE, 1);
	
	public static ItemStack SLIME_BALL = new ItemStack(Material.SLIME_BALL, 5);
	
	public static ItemStack RED_WOOL = new ItemStack(Material.WOOL, 1, (short) DyeColor.RED.getWoolData());
	public static ItemStack BLUE_WOOL = new ItemStack(Material.WOOL, 1, (short) DyeColor.BLUE.getWoolData());
	
	public static ItemStack STEAK = new ItemStack(Material.COOKED_BEEF);
	
	public static final Permission CMD_JOIN_PERM = new Permission("mccod.command.play");
	public static final Permission CMD_LEAVE_PERM = new Permission("mccod.command.play");
	public static final Permission CMD_ARENA_PERM = new Permission("mccod.command.arena");
	public static final Permission CMD_MAP_PERM = new Permission("mccod.command.map");
	public static final Permission CMD_SETSPAWN_PERM = new Permission("mccod.command.setspawn");
	public static final Permission CMD_START_PERM = new Permission("mccod.command.start");
	public static final Permission CMD_END_PERM = new Permission("mccod.command.end");
	
	public static final Permission SIGN_PERM = new Permission("mccod.sign");
	
	public static final int ARCHER_STEAK_AMOUNT = 4;
	public static final int ASSASSIN_STEAK_AMOUNT = 0;
	public static final int HEAVY_STEAK_AMOUNT = 3;
	public static final int MEDIC_STEAK_AMOUNT = 6;
	public static final int NECRO_STEAK_AMOUNT = 5;
	public static final int NINJA_STEAK_AMOUNT = 0;
	public static final int PYRO_STEAK_AMOUNT = 5;
	public static final int SOLDIER_STEAK_AMOUNT = 4;
	
	public Localization(){
		init();
	}
	
	public static void init(){
		//RED ARMOR
		LeatherArmorMeta helmetRed = (LeatherArmorMeta) LEATHER_HELMET_RED.getItemMeta();
		helmetRed.setColor(Color.RED);
		LEATHER_HELMET_RED.setItemMeta(helmetRed);

		LeatherArmorMeta chestplateRed = (LeatherArmorMeta) LEATHER_CHESTPLATE_RED.getItemMeta();
		chestplateRed.setColor(Color.RED);
		LEATHER_CHESTPLATE_RED.setItemMeta(chestplateRed);

		LeatherArmorMeta leggingsRed = (LeatherArmorMeta) LEATHER_LEGGINGS_RED.getItemMeta();
		leggingsRed.setColor(Color.RED);
		LEATHER_LEGGINGS_RED.setItemMeta(leggingsRed);

		LeatherArmorMeta bootsRed = (LeatherArmorMeta) LEATHER_BOOTS_RED.getItemMeta();
		bootsRed.setColor(Color.RED);
		LEATHER_BOOTS_RED.setItemMeta(bootsRed);
		
		//BLUE ARMOR
		LeatherArmorMeta helmetBlue = (LeatherArmorMeta) LEATHER_HELMET_BLUE.getItemMeta();
		helmetBlue.setColor(Color.BLUE);
		LEATHER_HELMET_BLUE.setItemMeta(helmetBlue);

		LeatherArmorMeta chestplateBlue = (LeatherArmorMeta) LEATHER_CHESTPLATE_BLUE.getItemMeta();
		chestplateBlue.setColor(Color.BLUE);
		LEATHER_CHESTPLATE_BLUE.setItemMeta(chestplateBlue);

		LeatherArmorMeta leggingsBlue = (LeatherArmorMeta) LEATHER_LEGGINGS_BLUE.getItemMeta();
		leggingsBlue.setColor(Color.BLUE);
		LEATHER_LEGGINGS_BLUE.setItemMeta(leggingsBlue);

		LeatherArmorMeta bootsBlue = (LeatherArmorMeta) LEATHER_BOOTS_BLUE.getItemMeta();
		bootsBlue.setColor(Color.BLUE);
		LEATHER_BOOTS_BLUE.setItemMeta(bootsBlue);
		
		//ITEM NAMES
		ItemMeta redWool = RED_WOOL.getItemMeta();
		redWool.setDisplayName(ChatColor.RED + "Choose a Gun");
		RED_WOOL.setItemMeta(redWool);
		
		ItemMeta blueWool = BLUE_WOOL.getItemMeta();
		blueWool.setDisplayName(ChatColor.BLUE + "Choose a Gun");
		BLUE_WOOL.setItemMeta(blueWool);
	}
}