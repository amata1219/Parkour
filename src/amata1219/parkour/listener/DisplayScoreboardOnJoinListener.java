package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.Main;

public class DisplayScoreboardOnJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Main.getUserSet().getUser(event.getPlayer()).scoreboard.loadScoreboard();
	}

}
