package de.fredo121.lobby.events;

import org.bukkit.ChatColor;

import org.bukkit.entity.Entity;  

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.block.BlockBreakEvent; 
import org.bukkit.event.block.BlockPlaceEvent; 
import org.bukkit.event.entity.EntityDamageEvent; 
import org.bukkit.event.weather.WeatherChangeEvent; 
import org.spigotmc.event.player.PlayerSpawnLocationEvent; 


import de.fredo121.lobby.main.Lobby;
import de.fredo121.lobby.main.Settings;

public class PlayerEventListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(ChatColor.DARK_GRAY + "Join> " + ChatColor.WHITE + event.getPlayer().getName()); 
		Lobby.get().addPlayer(event.getPlayer());
	} 

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(ChatColor.DARK_GRAY + "Quit> " + ChatColor.WHITE + event.getPlayer().getName());            
		Lobby.get().removePlayer(event.getPlayer()); 
	} 
	
	@EventHandler
	public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event){ 
		if(Lobby.get().isLobbyEnabled()){ 
			event.setSpawnLocation(Settings.get().getLobbyLocation()); 
		}
	} 
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event){ 
		if(Lobby.get().isLobbyEnabled()){ 
			if(event.getCause() == EntityDamageEvent.DamageCause.VOID){ 
				Entity entity = event.getEntity(); 
				if(entity != null){ 
					entity.teleport(Settings.get().getLobbyLocation()); 
				}
			} 
			event.setCancelled(true); 
		} 
	} 
	
	
	
	@EventHandler 
	public void onBlockBreakEvent(BlockBreakEvent event){
		if(Lobby.get().isLobbyEnabled()){
			event.setCancelled(true); 
		}
	} 
	
	@EventHandler 
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		if(Lobby.get().isLobbyEnabled()){
			event.setCancelled(true); 
		}
	} 
	
	@EventHandler 
	public void onWeatherChangeEvent(WeatherChangeEvent event){
		if(Lobby.get().isLobbyEnabled()){
			event.setCancelled(true); 
		}
	}
}
