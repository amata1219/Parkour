package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class LoadUserDataListener implements Listener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void applyUserInformation(PlayerJoinEvent event){
		User user = users.getUser(event.getPlayer());

		//表示名を書き換える
		ApplyRankToDisplayName.apply(user);

		//スコアボードを表示する
		user.informationBoard.loadScoreboard();

		//
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		User user = users.getUser(event.getPlayer());

		user.informationBoard.clearScoreboard();
	}

}
