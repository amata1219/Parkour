package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class JoinPlayerListener implements Listener {

	private final UserSet userSet = UserSet.getInstnace();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		//初参加であればユーザーデータを作成する
		userSet.onPlayerFirstJoin(player);

		//ユーザーデータを取得する
		User user = userSet.getUser(player);

		//プレイヤー名にランクを付け加える
		user.applyRankToPlayerName();
	}

}
