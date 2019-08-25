package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.parkour.function.ToggleHideMode;

public class HideNewPlayerListener implements PlayerJoinListener, PlayerQuitListener {

	private final ToggleHideMode function = ToggleHideMode.getInstance();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		function.onPlayerJoin(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		function.onPlayerQuit(event.getPlayer());
	}

}
