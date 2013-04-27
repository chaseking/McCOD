package com.chasechocolate.mccod.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.chasechocolate.mccod.McCOD;
import com.chasechocolate.mccod.game.GameUtils;
import com.chasechocolate.mccod.game.arena.Arena;
import com.chasechocolate.mccod.game.arena.ArenaUtils;

public class Turret {
	private McCOD plugin;
	
	private String owner;
	
	private int id;
	private final int distance = 15;
	
	private boolean enabled = false;
	
	private Location loc;
	
	private BlockState oldState;
	private BlockState oldStateUp;
	
	public Turret(McCOD plugin, String owner, int id, Location loc){
		this.plugin = plugin;
		this.owner = owner;
		this.id = id;
		this.loc = loc;
		this.oldState = loc.getBlock().getRelative(BlockFace.UP).getState();
		this.oldStateUp = loc.getBlock().getRelative(BlockFace.UP, 2).getState();
	}
	
	public void construct(){
		loc.getBlock().getRelative(BlockFace.UP).setType(Material.FENCE);
		loc.getBlock().getRelative(BlockFace.UP, 2).setType(Material.DISPENSER);
		
		enabled = true;
	}
	
	public void destroy(){
		loc.getBlock().getRelative(BlockFace.UP).setType(oldState.getType());
		loc.getBlock().getRelative(BlockFace.UP, 2).setType(oldStateUp.getType());
		
		oldState.update();
		oldStateUp.update();
		
		//TODO remove turret
		
		enabled = false;
	}
	
	public void startShooting(){
		new BukkitRunnable(){
			@Override
			public void run(){
				if(getAmmoLeft() > 0){
					final Location loc = getTurretBlock().getLocation();
					final Bat bat = (Bat) loc.getWorld().spawnEntity(loc, EntityType.BAT);
					final Player engineer = Bukkit.getPlayerExact(owner);
					
					for(Entity entity : bat.getNearbyEntities(distance, distance, distance)){
						if(entity instanceof Player){
							Player nearby = (Player) entity;
							
							if(GameUtils.isInGame(engineer) && GameUtils.isInGame(nearby)){
								Arena engineerArena = ArenaUtils.getPlayerArena(engineer);
								Arena nearbyArena = ArenaUtils.getPlayerArena(nearby);
								
								if(engineerArena.equals(nearbyArena)){
									if(!(ArenaUtils.onSameTeam(engineer, nearby))){
										Arrow arrow = bat.launchProjectile(Arrow.class);
										arrow.setVelocity(getBulletVelocity(bat.getLocation(), nearby.getLocation()));
										bat.remove();
										
										break;
									}
								}
							}
						}
					}
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 12L);
	}
	
	public int getAmmoLeft(){
		BlockState block = loc.getBlock().getRelative(BlockFace.UP, 2).getState();
		int ammo = 0;
		
		if(block instanceof Dispenser){
			Dispenser dispenser = (Dispenser) block;
			Inventory inv = dispenser.getInventory();
			
			for(ItemStack item : inv.getContents()){
				if(item.getType() == Material.ARROW){
					ammo += item.getAmount();
				}
			}
		}
		
		return ammo;
	}
	
	public void setAmmo(int ammo){
		BlockState block = loc.getBlock().getRelative(BlockFace.UP, 2).getState();
		
		if(block instanceof Dispenser){
			Dispenser dispenser = (Dispenser) block;
			Inventory inv = dispenser.getInventory();
			
			int maxStackSize = Material.ARROW.getMaxStackSize();
			int stacks = ammo / maxStackSize;
			int leftOver = ammo % maxStackSize;
			
			inv.clear();
			
			for(int i = 0; i < stacks; i++){
				inv.addItem(new ItemStack(Material.ARROW, maxStackSize));
			}
			
			inv.addItem(new ItemStack(Material.ARROW, leftOver));
		}
	}
	
	public String getOwner(){
		return this.owner;
	}
	
	public Location getLocation(){
		return this.loc;
	}
	
	public int getId(){
		return this.id;
	}
	
	public boolean isEnabled(){
		return this.enabled;
	}
	
	public Block getTurretBlock(){
		Block block = loc.add(0, 2, 0).getBlock();
		return block;
	}
	
	public Vector getBulletVelocity(Location shooterLoc, Location toShoot){
		Vector velocity = toShoot.toVector().subtract(shooterLoc.toVector());
		return velocity;
	}
}