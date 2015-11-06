package ch.toothwit.lobby.main;

import java.util.ArrayList;
import java.util.List;

import ch.toothwit.lobby.main.Lobby;
import ch.toothwit.lobby.main.Settings; 
import ch.toothwit.lobby.main.LobbyEventHandler; 

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
	
	public static void test(){
		Lobby.get().reload();
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
