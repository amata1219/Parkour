package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

import amata1219.parkour.function.hotbar.ControlFunctionalHotbarItem;

public class PlayerLocaleChangeListener implements Listener {

	@EventHandler
	public void onChange(PlayerLocaleChangeEvent event){
		Player player = event.getPlayer();

		//各アイテムのテキストを使用言語に対応させる
		for(int slotIndex = 0; slotIndex < 10; slotIndex++) ControlFunctionalHotbarItem.updateSlot(player, slotIndex);
	}

}
