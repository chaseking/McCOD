package com.chasechocolate.mccod.game;

import java.util.Random;

import org.bukkit.entity.Player;

import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class GameUtils {
	private static Random random = new Random();
	
	public static TeamColor chooseTeam(Arena arena){
		int redSize = arena.getPlayersOnTeam(TeamColor.RED).size();
		int blueSize = arena.getPlayersOnTeam(TeamColor.BLUE).size();
		boolean goOnRed = random.nextBoolean();
		
		if(redSize > blueSize){
			return TeamColor.BLUE;
		} else if(blueSize > redSize){
			return TeamColor.RED;
		} else {
			if(goOnRed){
				return TeamColor.RED;
			} else {
				return TeamColor.BLUE;
			}
		}
	}
	
	public static boolean isInGame(Player player){
		boolean isInArena = ArenaUtils.getAllActivePlayers().contains(player);
		return isInArena;
	}
}