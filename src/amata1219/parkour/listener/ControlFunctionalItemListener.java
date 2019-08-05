package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class ControlFunctionalItemListener {

	/*
	 * last cp
	 *
	 * show/hide
	 *
	 * menu/setting
	 *
	 */

	@EventHandler
	public void initializeSlots(PlayerJoinEvent event){

	}

	@EventHandler
	public void clickSlot(PlayerInteractEvent event){

	}

	@EventHandler
	public void controlSlot(InventoryClickEvent event){

	}

	@EventHandler
	public void changeSlots(PlayerGameModeChangeEvent event){

	}

	@EventHandler
	public void clearSlots(PlayerQuitEvent event){

	}

	public void initializeSlots(Inventory inventory){

	}

	public void clearSlots(Inventory inventory){

	}

}
