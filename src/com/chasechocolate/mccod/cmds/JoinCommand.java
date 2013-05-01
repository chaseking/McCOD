package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class JoinCommand extends CODCommand {	
	public JoinCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			//Usage: /cod join <arena>
			if(args.length == 2){
				String arenaName = args[1];
				if(!(ArenaUtils.arenaExists(arenaName))){
					player.sendMessage(ChatColor.RED + "The specified arena does not exist or is not enabled!");
					return;
				}
				
				if(GameUtils.isInGame(player)){
					player.sendMessage(ChatColor.RED + "You are already in a game! You must leave the game to join another!");
					return;
				}
				
				if(!(GameUtils.lobbyExists())){
					player.sendMessage(ChatColor.RED + "Failed to join the game! The lobby location hasn't been set yet!");
					return;
				}
				
				Arena arena = ArenaUtils.getArena(arenaName);
				arena.addPlayer(player);
			} else {			
				helper.wrongArguments();
				return;
			}
		} else {
			helper.noConsole();
			return;
		}
	}
}