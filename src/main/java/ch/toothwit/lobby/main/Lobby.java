package ch.toothwit.lobby.main;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ch.toothwit.lobby.events.PlayerEventListener;

import org.bukkit.Bukkit; 
import org.bukkit.GameMode;

import net.md_5.bungee.api.ChatColor;

public class Lobby extends JavaPlugin {
	private static Lobby instance;
	private boolean enabled;   
	private List<Player> players;  
	private BukkitRunnable startgameTask; 
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	
		this.reload(); 
	
		getLogger().info("Lobby was enabled");
	} 

	@Override 
	public void onDisable() { 
		Util.hideCountdown(); 
		getLogger().info("Lobby was disabled");
	}

	public void reload(){ 
		enabled = true; 
		players = new ArrayList<Player>(); 
		startgameTask = null; 
		
		
		for(Player p : Bukkit.getOnlinePlayers()){ 
			p.setGameMode(GameMode.SURVIVAL); 
			p.setHealth(p.getMaxHealth()); 
			p.setFoodLevel(20); 
			this.addPlayer(p); 
		}
		
		Bukkit.getLogger().info("Restarted "); 
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("gamelobby")) {
			Player p = (Player) sender;

			if (args.length == 0) {
				
				return true;
			} else { 
				String cmd = args[0].toLowerCase();
				if (p.hasPermission("lobby.admin") || p.isOp()) {
					if (cmd.equals("setlocation")) {
						Settings.get().setLobbyLocation(p.getLocation());
						sender.sendMessage("Lobby location was set to your location.");
					} else if (cmd.equals("setminplayers")) {
						Settings.get().setMinPlayers(Integer.parseInt(args[1]));
						sender.sendMessage("Set MinPlayers to" + args[1]);
					} else if (cmd.equals("setmaxplayers")) {
						Settings.get().setMaxPlayers(Integer.parseInt(args[1]));
						sender.sendMessage("Set maxPlayers to" + args[1]);
					} else if (cmd.equals("setcountdown")) {
						Settings.get().setLobbyCountdown(Integer.parseInt(args[1]));
						sender.sendMessage("Set lobby countdown to " + args[1]);
					} else if (cmd.equals("reload")) {
						Settings.get().reloadConfig();
						sender.sendMessage("Reloaded configuration");
					} else if (cmd.equals("help") || cmd.equals("?")) {
						displayUsage(sender);
					} else if (cmd.equals("start")) {
						startGame(); 
						sender.sendMessage("Started the game");
					} else if(cmd.equals("stop")) { 
						this.enabled = false; 
						sender.sendMessage("Stopped Lobby");  
					} else {
						displayUsage(sender);
					}
				} 
				return true;
			}
		}
		return false; 
	}

	private void startGame(){ 
		enabled = false; 
		LobbyAPI.startGame(); 
	}
	
	private void displayUsage(CommandSender sender) {
		sender.sendMessage(new String[] { "Available commands: ", "/lobby help", "/lobby setLocation",
				"/lobby setMinPlayers", "/lobby setMaxPlayers", "/lobby setCountdown" });
	}

	public void addPlayer(Player player) {
		if (players.size() >= Settings.get().getMaxPlayers()) { 
			player.sendMessage("Teleportiere zur\u00DCck zur Lobby."); 
			Util.SendToBungeeServer(Settings.get().getBungeeLobbyServer(), player);
		} else {
			player.getPlayer().setGameMode(GameMode.SURVIVAL);
			player.sendMessage(ChatColor.GOLD+"Willkommen in der Lobby"); 
			players.add(player);
			updateState();
		}
	}

	public void removePlayer(Player player) {
		players.remove(player);
		updateState();
	}

	public List<Player> getPlayers() {
		return players;
	}

	private void updateState() { 
		if (players.size() >= Settings.get().getMinPlayers()) { 
			if(startgameTask == null){ 
				Bukkit.broadcastMessage(ChatColor.GOLD+"Spiel startet in "+ChatColor.RED+Settings.get().getLobbyCountdown()+ChatColor.GOLD+" Sekunden."); 
				displayCountdown(Settings.get().getLobbyCountdown(), Settings.get().getLobbyCountdown(), "Starte spiel"); 
			} 
		} else { 
			Util.displayMessage("Warte auf Spieler"); 
			hideCountdown(); 
		} 
	}

	private void displayCountdown(int currentSeconds, final int startSeconds, final String message) {
		Bukkit.getLogger().info("Started Countdown"); 
		Util.displayCountdown(startSeconds, message); 
		startgameTask = new BukkitRunnable() {

			public void run() {
				startGame(); 
			} 
		}; 
		startgameTask.runTaskLater(instance, currentSeconds*20L); 
	}

	private void hideCountdown() { 
		Bukkit.getLogger().info("Stopped Countdown"); 
		if(startgameTask != null){      
			Util.hideCountdown(); 
			startgameTask.cancel(); 
		}
		startgameTask = null; 
	}

	public static Lobby get() {
		return instance;
	}

	public Lobby() {
		instance = this;
	}

	public boolean isLobbyEnabled() {
		return enabled;
	}
}
