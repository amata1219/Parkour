package amata1219.parkour.listener;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.Main;
import amata1219.parkour.user.UserSet;

public class CreateUserInstanceListener implements Listener {

	private final UserSet userSet = Main.getUserSet();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		UUID uuid = event.getPlayer().getUniqueId();

		if(userSet.users.containsKey(uuid))
			userSet.registerNewUser(uuid);
	}

}
