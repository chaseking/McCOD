package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameStatus;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class StartCommand extends CODCommand {
	public StartCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		Player player = (Player) sender;
		
		//Usage: /cod end <arena-name>
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("start")){
				if(ArenaUtils.isArena(args[1])){
					Arena arena = ArenaUtils.getArena(args[1]);
					player.sendMessage(ChatColor.GREEN + "Attempting to start the arena '" + arena.getName() + "'...");
					
					if(arena.getStatus() == GameStatus.INGAME){
						player.sendMessage(ChatColor.RED + "The arena '" + arena.getName() + "' has already started!");
						return;
					} else {
						arena.start();
						return;
					}
				} else {
					player.sendMessage(ChatColor.RED + "Could not find the arena '" + args[1] + "'! It either doesn't exist or is not running!");
					return;
				}
			}
			
			helper.wrongArguments();
			return;
		}
		
		helper.unknownCommand();
		return;
	}
}