package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class ApplyUserSettingsListener implements Listener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void applyUserSettings(PlayerJoinEvent event){
		User user = users.getUser(event.getPlayer());

		//

		//スコアボードを表示する
		user.informationBoard.loadScoreboard();

		//
	}

}
