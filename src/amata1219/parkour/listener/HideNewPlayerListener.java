package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import amata1219.parkour.function.ToggleHideModeChange;

public class HideNewPlayerListener implements Listener {

	private final ToggleHideModeChange function = ToggleHideModeChange.getInstance();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		function.onPlayerJoin(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		function.onPlayerQuit(event.getPlayer());
	}

}
