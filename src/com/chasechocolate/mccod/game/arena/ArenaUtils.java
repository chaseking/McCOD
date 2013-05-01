package com.chasechocolate.mccod.game.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.chasechocolate.mccod.game.CODPlayer;
import com.chasechocolate.mccod.game.map.Map;
import com.chasechocolate.mccod.utils.PlayerUtils;

public class ArenaUtils {
	private static List<Arena> runningArenas = new ArrayList<Arena>();
	
	public static List<Arena> getCurrentArenas(){
		return runningArenas;
	}
	
	public static void addArena(Arena arena){
		if(!(runningArenas.contains(arena))){			
			runningArenas.add(arena);
		}
	}
	
	public static void removeArena(Arena arena){
		if(runningArenas.contains(arena)){
			runningArenas.remove(arena);
		}
	}
	
	public static Arena getPlayerArena(Player player){
		Arena playerArena = null;
		
		for(Arena arena : getCurrentArenas()){
			if(arena.getAllPlayers().contains(player)){
				playerArena = arena;
			}
		}
		
		return playerArena;
	}
	
	public static Arena getArena(String name){
		Arena arena = null;
		
		for(Arena allArenas : getCurrentArenas()){
			if(allArenas.getName().equals(name)){
				arena = allArenas;
			}
		}
		
		return arena;
	}
	
	public static boolean arenaExists(String name){
		boolean exists = false;
		
		for(Arena allArenas : getCurrentArenas()){
			if(allArenas.getName().equals(name)){
				exists = true;
			}
		}
		
		return exists;
	}
	
	public static boolean onSameTeam(Player player1, Player player2){
		/*
		if(getPlayerArena(player1).equals(getPlayerArena(player2))){
			Arena arena = getPlayerArena(player1);
			if(arena.getPlayersOnTeam(TeamColor.RED).contains(player1) && arena.getPlayersOnTeam(TeamColor.RED).contains(player2)){
				return true;
			} else if(arena.getPlayersOnTeam(TeamColor.BLUE).contains(player1) && arena.getPlayersOnTeam(TeamColor.BLUE).contains(player2)){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		*/
		
		CODPlayer codPlayer1 = PlayerUtils.getCODPlayer(player1);
		CODPlayer codPlayer2 = PlayerUtils.getCODPlayer(player2);
		
		if(codPlayer1.getArena().equals(codPlayer2.getArena())){
			if(codPlayer1.getTeam() == codPlayer2.getTeam()){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean mapRunning(Map map){
		for(Arena arena : getCurrentArenas()){
			if(arena.getMap() == map){
				return true;
			}
		}
		
		return false;
	}
}