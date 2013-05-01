package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class LeaveCommand extends CODCommand {
	public LeaveCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			if(GameUtils.isInGame(player)){
				ArenaUtils.getPlayerArena(player).removePlayer(player);
				return;
			} else {
				player.sendMessage(ChatColor.RED + "You are currently not in a game!");
				return;
			}
		}
	}
}