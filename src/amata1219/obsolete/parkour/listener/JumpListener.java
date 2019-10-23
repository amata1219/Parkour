package amata1219.obsolete.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import amata1219.beta.parkour.event.PlayerJumpEvent;
import amata1219.obsolete.parkour.user.UserSet;

public class JumpListener implements Listener {

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		UserSet.getInstnace().getUser(event.getPlayer()).statusBoard.updateJumps();
	}

}
