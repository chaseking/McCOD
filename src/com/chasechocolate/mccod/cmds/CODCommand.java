package com.chasechocolate.mccod.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chasechocolate.mccod.McCOD;

public abstract class CODCommand {
	protected McCOD plugin;
	
	public CODCommand(McCOD plugin){
		this.plugin = plugin;
	}
	
	public abstract void executeCommand(CommandSender sender, Command cmd, String[] args);
}