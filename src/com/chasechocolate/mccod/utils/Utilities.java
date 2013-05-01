package com.chasechocolate.mccod.utils;

import java.util.Map.Entry;
import java.util.SortedMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utilities {
	public static void paginate(CommandSender sender, SortedMap<Integer, String> map, int page, int pageLength){ //Credits to gomeow
		sender.sendMessage(ChatColor.YELLOW + "List: Page (" + String.valueOf(page) + " of " + (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1));
		int i = 0;
		int k = 0;
		page--;
		
		for(final Entry<Integer, String> e : map.entrySet()){
			k += 1;
			if((((page * pageLength) + i + 1) == k) && (k != ((page * pageLength) + pageLength + 1))){
				i += 1;
				sender.sendMessage(ChatColor.YELLOW + " - " + e.getValue());
			}
		}
	}
	
	public static boolean isNumber(String text){
		try{
			Integer.parseInt(text);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}
}