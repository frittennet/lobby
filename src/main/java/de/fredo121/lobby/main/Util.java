package de.fredo121.lobby.main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable; 
import org.bukkit.Bukkit;  

import de.fredo121.lobby.main.Lobby; 

import org.inventivetalent.bossbar.BossBarAPI; 

public class Util { 
	public static void SendToBungeeServer(final String server, final Player player) { 
		new BukkitRunnable() { 
			
			public void run() {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);

                try {
                    out.writeUTF("Connect");
                    out.writeUTF(server);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (b != null) {
                    player.sendPluginMessage(Lobby.get(), "BungeeCord", b.toByteArray());
                }
            }
        }.runTaskLater(Lobby.get(), 20L); 
	} 
	
	private static BukkitRunnable countdownTask; 
	
	public static void displayCountdown(final int startSeconds, final String message){ 
		displayMessage(message); 
		
		countdownTask = new BukkitRunnable() { 
			private int count = 0; 
			
			public void run() { 
				count++; 
				if((startSeconds-count) <= 0){
					Util.hideCountdown(); 
				}
				for(Player player : Bukkit.getOnlinePlayers()){ 
					float precentage =  (((float)(startSeconds-count)/(float)startSeconds)*(float)100);                       
					BossBarAPI.setHealth(player, precentage); 
					player.setLevel(startSeconds-count); 
				} 
			}
		};  
		countdownTask.runTaskTimer(Lobby.get(), 0L, 20L); 
	}  
	
	public static void displayMessage(String message){ 
		hideCountdown(); 
		for(Player player : Bukkit.getServer().getOnlinePlayers()){ 
			BossBarAPI.setMessage(player, message); 
		} 
	} 
	
	public static void hideCountdown(){ 
		if(countdownTask != null){ 
			countdownTask.cancel(); 
		}
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.setLevel(0); 
			if(BossBarAPI.hasBar(player)){ 
				BossBarAPI.removeBar(player); 
			}
		}
	} 
}
