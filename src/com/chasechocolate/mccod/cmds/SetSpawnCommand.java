package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.game.map.MapUtils;
import com.chasechocolate.mccod.utils.LocationUtils;

public class SetSpawnCommand extends CODCommand {	
	public SetSpawnCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		Player player = (Player) sender;
		Location loc = player.getLocation();
		
		//Usage: /cod setspawn lobby
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("setspawn")){
				if(args[1].equalsIgnoreCase("lobby")){
					LocationUtils.setLobbyLoc(loc);
					player.sendMessage(ChatColor.GREEN + "Successfully set the lobby spawn location!");
					
					return;
				}
			}
		}
		
		//Usage: /cod setspawn map <map> <red/blue>
		if(args.length == 4){
			if(args[0].equalsIgnoreCase("setspawn")){
				if(args[1].equalsIgnoreCase("map")){
					if(MapUtils.isMap(args[2])){
						Map map = MapUtils.getMap(args[2]);
						
						if(args[3].equalsIgnoreCase("red")){
							map.setRedSpawn(loc);
							player.sendMessage(ChatColor.GREEN + "Successfully set the " + args[3].toLowerCase() + " team's spawn location in the map '" + args[2] + "'!");
							
							return;
						} else if(args[3].equalsIgnoreCase("blue")){
							map.setBlueSpawn(loc);
							player.sendMessage(ChatColor.GREEN + "Successfully set the " + args[3].toLowerCase() + " team's spawn location in the map '" + args[2] + "'!");
							
							return;
						} else {
							player.sendMessage(ChatColor.RED + "Unknown team, use either red or blue.");
							return;
						}
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