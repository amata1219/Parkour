package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class DisplayRegionBorderListener implements PlayerJoinListener, PlayerQuitListener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		//ユーザーデータを取得する
		User user = users.getUser(player);

		//最後にいたアスレを取得する
		Parkour lastParkour = user.currentParkour;

		//あれば参加させる
		if(lastParkour != null) lastParkour.entry(user);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();

		//ユーザーデータを取得する
		User user = users.getUser(player);

		//今いるアスレを取得する
		Parkour currentParkour = user.currentParkour;

		//あれば退出させる
		if(currentParkour != null) currentParkour.exit(user);
	}

}
