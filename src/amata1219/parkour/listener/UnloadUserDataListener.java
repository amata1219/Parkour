package amata1219.parkour.listener;

import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class UnloadUserDataListener implements PlayerQuitListener {

	@Override
	public void onQuit(PlayerQuitEvent event) {
		User user = Users.getInstnace().getUser(event.getPlayer());
		user.inventoryUserInterfaces = null;
	}

}
