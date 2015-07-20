package de.fredo121.lobby.main;

import java.util.ArrayList;
import java.util.List;

import de.fredo121.lobby.main.Settings; 
import de.fredo121.lobby.main.Lobby; 

public class LobbyAPI {
	private static LobbyAPI instance; 
	
	private List<LobbyEventHandler> subscribers = new ArrayList<LobbyEventHandler>(); 
	
	private static LobbyAPI get(){
		if(instance == null){
			instance = new LobbyAPI(); 
		} 
		return instance; 
	} 
	
	public static void subscribe(LobbyEventHandler eventHandler){
		get().subscribers.add(eventHandler); 
	}
	
	public static String getBungeeLobbyServer(){
		return Settings.get().getBungeeLobbyServer(); 
	}
	
	public static void startGame(){
		for(LobbyEventHandler eventHandler : get().subscribers){
			eventHandler.StartGame(Lobby.get().getPlayers());
		}
	} 
	
	public static void reload(){
		Lobby.get().reload(); 
	}
}
