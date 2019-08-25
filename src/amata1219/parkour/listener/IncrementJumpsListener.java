package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import amata1219.parkour.event.PlayerJumpEvent;
import amata1219.parkour.user.Users;

public class IncrementJumpsListener implements Listener {

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		Users.getInstnace().getUser(event.getPlayer()).statusBoard.updateJumps();
	}

}
