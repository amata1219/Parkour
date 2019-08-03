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

		//ユーザーデータを取得する
		User user = userSet.getUser(player);

		//ユーザーデータが存在しなければ新しく作成する
		if(user == null)
			userSet.registerNewUser(player);

		//プレイヤー名にランクを付け加える
		user.applyRankToPlayerName();
	}

}
