package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.Localization;

public class CommandManager implements CommandExecutor {
	private McCOD plugin;
	
	public CommandManager(McCOD plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		if(args.length == 0){
			sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "-=- Minecraft Call of Duty -=-");
			sender.sendMessage(ChatColor.RED + "Plugin developed by: Nauss (IGN) or chasechocolate (Bukkit)");
			sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.DARK_AQUA + "/cod help" + ChatColor.AQUA + " for plugin help.");
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("help")){
			plugin.gameManager.givePluginInfo(sender);
		}
		
		if(args[0].equalsIgnoreCase("join")){
			if(sender.hasPermission(Localization.CMD_JOIN_PERM)){
				if(sender instanceof Player){					
					new JoinCommand(plugin).executeCommand(sender, cmd, args);
				} else {
					helper.noConsole();
				}
			} else {
				helper.noPermission();
			}
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("leave")){
			if(sender.hasPermission(Localization.CMD_LEAVE_PERM)){
				if(sender instanceof Player){
					new LeaveCommand(plugin).executeCommand(sender, cmd, args);
				} else {
					helper.noConsole();
				}
			} else {
				helper.noPermission();
			}
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("map")){
			if(sender.hasPermission(Localization.CMD_MAP_PERM)){
				if(sender instanceof Player){
					new MapCommand(plugin).executeCommand(sender, cmd, args);
				} else {
					helper.noConsole();
				}
			} else {
				helper.noPermission();
			}
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setspawn")){
			if(sender.hasPermission(Localization.CMD_SETSPAWN_PERM)){
				if(sender instanceof Player){
					new SetSpawnCommand(plugin).executeCommand(sender, cmd, args);
				} else {
					helper.noConsole();
				}
			} else {
				helper.noPermission();
			}
			
			return true;
		}
		
		helper.unknownCommand();
		
		return true;
	}
}