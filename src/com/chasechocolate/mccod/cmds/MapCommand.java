package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.map.MapUtils;
import com.chasechocolate.mccod.utils.WorldEditUtils;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class MapCommand extends CODCommand {
	public MapCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		Player player = (Player) sender;
		
		//Usage: /cod map <create/delete> <name>
		if(args.length == 3){
			if(args[0].equalsIgnoreCase("map")){
				if(args[1].equalsIgnoreCase("create")){
					if(MapUtils.isMap(args[2])){
						player.sendMessage(ChatColor.RED + "The map '" + args[2] + "' already exists!");
						
						return;
					} else {
						Selection selection = WorldEditUtils.getWorldEdit().getSelection(player);
						
						if(selection != null){
							MapUtils.createMap(args[2], selection);
							player.sendMessage(ChatColor.GREEN + "Created map with the name '" + args[2] + "'! Now set the team spawns!");
							
							return;
						} else {
							player.sendMessage(ChatColor.RED + "Select a region with WorldEdit first!");
							return;
						}						
					}
				} else if(args[1].equalsIgnoreCase("delete")){
					if(MapUtils.isMap(args[2])){
						MapUtils.deleteMap(args[2]);
						player.sendMessage(ChatColor.GREEN + "Successfully deleted the map with the name '" + args[2] + "'!");
						return;
					} else {
						player.sendMessage(ChatColor.RED + "Could not find the map '" + args[2] + "'!");
						return;
					}
				}
			}
			
			helper.wrongArguments();
			return;
		}
		
		helper.unknownCommand();
		
		return;
	}
}