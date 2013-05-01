package com.chasechocolate.mccod.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.Gun;
import com.chasechocolate.mccod.utils.GunUtils;

public class GunCommand extends CODCommand {
	public GunCommand(McCOD plugin){
		super(plugin);
	}
	
	@Override
	public void executeCommand(CommandSender sender, Command cmd, String[] args){
		CommandHelper helper = new CommandHelper(sender, cmd);
		
		Player player = (Player) sender;
		
		//Usage: /cod gun <name>
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("gun")){
				if(GunUtils.isGun(args[1])){
					Gun gun = GunUtils.getGun(args[1]);
					
					if(GameUtils.isInGame(player)){
						GunUtils.setPlayerGun(player, gun);
						player.sendMessage(ChatColor.RED + "You have respawned with the gun: " + args[1] + "!");
						return;
					} else {
						player.sendMessage(ChatColor.RED + "You must be in a game to choose a gun!");
						return;
					}
				} else {
					player.sendMessage(ChatColor.RED + "Could not find the gun '" + args[1] + "'! To view a list of guns, type /cod gun!");
					return;
				}
			}
			
			helper.wrongArguments();
			return;
		}
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("gun")){
				GunUtils.showAvailableGuns(player);
				return;
			}
		}
		
		helper.unknownCommand();
		
		return;
	}
}