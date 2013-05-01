package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.utils.Utilities;

public class HelpCommand extends CODCommand {
	public HelpCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		int pageLength = plugin.commandHelp.size() + 1;
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				Utilities.paginate(sender, plugin.commandHelp, 1, pageLength);
			}
		}
		
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("help")){
				if(Utilities.isNumber(args[1])){
					int page = Integer.parseInt(args[1]);
					
					if(page >= 1 && page <= pageLength){
						Utilities.paginate(sender, plugin.commandHelp, page, pageLength);
						return;
					} else {
						sender.sendMessage(ChatColor.RED + "Failed to find page: " + page + "! Page must be inbetween 0 and " + pageLength + "!");
						return;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Could not recognize the number: " + args[1] + "!");
					return;
				}
			}
		}
		
		helper.wrongArguments();
		return;
	}
}