package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.TeamColor;

public class CODCommand implements CommandExecutor {
	private McCOD plugin;
	
	public CODCommand(McCOD plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("cod")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "In-game only!");
				return true;
			} else {
				Player player = (Player) sender;
				
				if(args.length == 0){
					//Usage: /cod
					player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "-=- Minecraft Call of Duty -=-");
					player.sendMessage(ChatColor.RED + "Plugin developed by: Nauss (IGN) or chasechocolate (Bukkit)");
					player.sendMessage(ChatColor.AQUA + "Use " + ChatColor.DARK_AQUA + "/cod help" + ChatColor.AQUA + " for plugin help.");
					return true;
				}
				
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("join")){
						//Usage: /cod join
						if(plugin.inGame.contains(player.getName())){
							player.sendMessage(plugin.chatTitle + ChatColor.GREEN + "You are already in a game!");
							return true;
						} else {
							plugin.gameManager.addToGame(player);
							return true;							
						}
					} else if(args[0].equalsIgnoreCase("leave")){
						//Usage: /cod leave
						if(plugin.inGame.contains(player.getName())){
							plugin.gameManager.removeFromGame(player);
							return true;
						} else {
							player.sendMessage(plugin.chatTitle + ChatColor.GREEN + "You are currently not in a game!");
							return true;
						}
					} else if(args[0].equalsIgnoreCase("help")){
						//Usage: /cod help
						plugin.gameManager.givePluginInfo(player);
						return true;
					}
				}
				
				if(args.length == 2){
					//Usage: /cod setspawn lobby
					if(args[0].equalsIgnoreCase("setspawn")){
						if(args[1].equalsIgnoreCase("lobby")){
							plugin.gameManager.setLobbySpawn(player);
							return true;
						}
					}
				}
				
				if(args.length == 3){
					//Usage: /cod map <create/delete> <name>
					if(args[0].equalsIgnoreCase("map")){
						if(player.hasPermission("mccod.command.map")){
							if(args[1].equalsIgnoreCase("create")){
								plugin.gameManager.createMap(args[2], player);
								return true;
							} else if(args[1].equalsIgnoreCase("delete")){
								plugin.gameManager.deleteMap(args[2], player);
								return true;
							} else {
								player.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Usage: " + ChatColor.DARK_AQUA + "/cod map <create/delete> <name>");
								return true;
							}
						} else {
							player.sendMessage(plugin.noPerms);
							return true;
						}
					}
				}
				
				if(args.length == 4){
					//Usage: /cod setspawn map <name> <red/blue>
					if(args[0].equalsIgnoreCase("setspawn")){
						if(args[1].equalsIgnoreCase("map")){
							if(plugin.locationsConfiguration.isConfigurationSection("maps." + args[2])){
								if(args[3].equalsIgnoreCase("red")){
									plugin.gameManager.setMapTeamSpawn(TeamColor.RED, args[2], player);									
									return true;
								} else if(args[3].equalsIgnoreCase("blue")){
									plugin.gameManager.setMapTeamSpawn(TeamColor.BLUE, args[2], player);
									return true;
								} else {
									player.sendMessage(plugin.chatTitle + ChatColor.GREEN + "Usage: " + ChatColor.DARK_AQUA + "/cod setspawn map " + args[2] + " <red/blue>");
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}