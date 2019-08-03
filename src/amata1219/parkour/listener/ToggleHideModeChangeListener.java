package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.function.ToggleHideModeChange;

public class ToggleHideModeChangeListener implements Listener {

	private final ToggleHideModeChange function = new ToggleHideModeChange();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		function.onPlayerJoin(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		function.onPlayerQuit(event.getPlayer());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.PHYSICAL)
			return;

		if(!event.hasItem())
			return;

		ItemStack item = event.getItem();

		function.change(event.getPlayer());
	}

}
