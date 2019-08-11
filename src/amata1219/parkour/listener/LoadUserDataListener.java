package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.user.InventoryUserInterfaces;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class LoadUserDataListener implements PlayerJoinListener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		User user = Users.getInstnace().getUser(event.getPlayer());

		ApplyRankToDisplayName.apply(user);
		user.inventoryUserInterfaces = new InventoryUserInterfaces(user);
	}

}
