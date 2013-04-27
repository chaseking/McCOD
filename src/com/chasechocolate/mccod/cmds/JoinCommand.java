package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class JoinCommand {
	@SuppressWarnings("unused")
	private McCOD plugin;
	
	public JoinCommand(McCOD plugin){
		this.plugin = plugin;
	}
	
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		@SuppressWarnings("unused")
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			if(args.length == 1){
				if(GameUtils.isInGame(player)){
					player.sendMessage(ChatColor.RED + "You are already in a game!");
					return;
				}
				
				if(null != null){ //TODO SEE IF THE LOBBY IS SET
					return;
				}
				
				return;
			}
			
			if(args.length == 2){
				String arena = args[1];
				if(!(ArenaUtils.arenaExists(arena))){
					player.sendMessage(ChatColor.RED + "The specified arena does not exist or is not enabled!");
					return;
				}
				
				if(GameUtils.isInGame(player)){
					player.sendMessage(ChatColor.RED + "You are already in a game!");
					return;
				}
				
				
			}
		}
	}
}