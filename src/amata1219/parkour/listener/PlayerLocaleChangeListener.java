package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

import amata1219.amalib.schedule.Sync;
import amata1219.parkour.function.hotbar.ControlFunctionalHotbarItem;
import amata1219.parkour.user.Users;

public class PlayerLocaleChangeListener implements Listener {

	public static void apply(Player player){
		for(int slotIndex = 0; slotIndex < 10; slotIndex++) ControlFunctionalHotbarItem.updateSlot(player, slotIndex);

		Users.getInstnace().getUser(player).board.updateAll();
	}

	@EventHandler
	public void onChange(PlayerLocaleChangeEvent event){
		Sync.define(() -> apply(event.getPlayer())).executeLater(100);
	}

}
