package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.map.MapUtils;

public class MapCommand {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public MapCommand(McCOD plugin){
		this.plugin = plugin;
	}
	
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		Player player = (Player) sender;
		
		//Usage: /cod map <create/delete> <name>
		if(args.length == 3){
			if(args[0].equalsIgnoreCase("map")){
				if(args[1].equalsIgnoreCase("create")){
					if(MapUtils.isMap(args[2])){
						
					} else {
						MapUtils.createMap(args[2]);
						
						player.sendMessage(ChatColor.RED + "Created map with the name '" + args[2] + "'! Now set the team spawns!");
						
						return;
					}
				} else if(args[1].equalsIgnoreCase("delete")){
					
				}
			}
			
			helper.wrongArguments();
			
			return;
		}
		
		helper.unknownCommand();
		
		return;
	}
}
