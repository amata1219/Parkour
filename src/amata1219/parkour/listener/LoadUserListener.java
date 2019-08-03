package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.Main;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class LoadUserListener implements Listener {

	private final UserSet userSet = Main.getUserSet();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		User user = userSet.getUser(player);

		if(user == null)
			userSet.registerNewUser(player.getUniqueId());

		user.applyRankToPlayerName();
	}

}
