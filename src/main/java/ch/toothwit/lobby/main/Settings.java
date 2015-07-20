package ch.toothwit.lobby.main;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import ch.toothwit.lobby.main.Lobby; 

public class Settings {
	private static Settings instance;
	private FileConfiguration config;
	private Location lobbyLocation;
	private int lobbyCountdown;
	private int minPlayers;
	private int maxPlayers;
	private String bungeeLobbyServer; 

	public static Settings get() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public Settings() { 
		Lobby.get().saveDefaultConfig(); 
		this.config = Lobby.get().getConfig(); 
		
		reloadConfig(); 
	}

	public void reloadConfig() {
		Lobby.get().reloadConfig(); 
		config = Lobby.get().getConfig(); 

		lobbyLocation = getLocation("lobby.location");
		lobbyCountdown = config.getInt("lobby.countdown");
		minPlayers = config.getInt("lobby.minPlayers");
		maxPlayers = config.getInt("lobby.maxPlayers"); 
		bungeeLobbyServer = config.getString("lobby.bungeeServer"); 
	}

	public void saveConfig() {
		setLocation("lobby.location", lobbyLocation);

		config.set("lobby.countdown", lobbyCountdown);
		config.set("lobby.minPlayers", minPlayers);
		config.set("lobby.maxPlayers", maxPlayers); 
		config.set("lobby.bungeeServer", bungeeLobbyServer); 

		File gameConfig = new File(Lobby.get().getDataFolder() + "/" + "config.yml");
		try {
			config.save(gameConfig);
		} catch (IOException e) {
			Bukkit.getLogger().warning("Could not save config");
		}
	}

	public void setLocation(String path, Location loc) {
		if (loc == null) {
			loc = new Location(Bukkit.getWorlds().get(0), 0d, 100d, 0d);
		}
		config.set(path, loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
	}

	public Location getLocation(String path) {
		String s = config.getString(path);
		return new Location(Bukkit.getWorld(s.split(" ")[0]), Double.parseDouble(s.split(" ")[1]),
				Double.parseDouble(s.split(" ")[2]), Double.parseDouble(s.split(" ")[3]));
	} 

	// get methods
	public int getMinPlayers() {
		return this.minPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getLobbyCountdown() {
		return lobbyCountdown;
	}

	public Location getLobbyLocation() {
		return lobbyLocation;
	}

	public String getBungeeLobbyServer() {
		return bungeeLobbyServer;
	}

	// set / add methods

	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
		saveConfig();
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
		saveConfig();
	}

	public void setLobbyLocation(Location location) {
		lobbyLocation = location;
		saveConfig();
	}

	public void setLobbyCountdown(int lobbyCountdown) {
		this.lobbyCountdown = lobbyCountdown;
		saveConfig(); 
	}

	public void setBungeeLobbyServer(String bungeeLobbyServer) {
		this.bungeeLobbyServer = bungeeLobbyServer; 
		saveConfig(); 
	}
	
}
