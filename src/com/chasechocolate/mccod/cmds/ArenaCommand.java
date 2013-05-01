package com.chasechocolate.mccod.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;

public class ArenaCommand extends CODCommand {
	public ArenaCommand(McCOD plugin){
		super(plugin);
	}

	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			//TODO
		}
	}
}