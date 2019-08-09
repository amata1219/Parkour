package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.parkour.user.Users;

public class LoadUserDataListener implements PlayerJoinListener {

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event){
		Users.getInstnace().getUser(event.getPlayer()).onJoin();
	}

}
