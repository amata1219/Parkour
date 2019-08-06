package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class DisableFoodLevelChangeListener implements Listener {

	@EventHandler
	public void disableFoodLevelChange(FoodLevelChangeEvent event){
		event.setCancelled(true);
	}

}
