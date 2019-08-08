package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class ApplyUserStateListener implements PlayerJoinListener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		User user = users.getUser(event.getPlayer());

		//表示名を書き換える
		ApplyRankToDisplayName.apply(user);
	}

}
