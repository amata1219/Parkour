package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import amata1219.amalib.event.PlayerJumpEvent;
import amata1219.parkour.user.UserSet;

public class IncrementJumpsListener implements Listener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		users.getUser(event.getPlayer()).board.updateJumps();
	}

}
