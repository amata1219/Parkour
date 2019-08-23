package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class UserQuitListener implements PlayerQuitListener {

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		User user = Users.getInstnace().getUser(event.getPlayer());

		//タイムアタックの途中であれば経過時間を記録する
		if(user.isPlayingWithParkour() && user.parkourPlayingNow.timeAttackEnable) user.timeElapsed = System.currentTimeMillis() - user.timeToStartPlaying;

		//今いるアスレを取得する
		Parkour currentParkour = user.currentParkour;

		//どこかのアスレにいるのであればそこから退出させる
		if(currentParkour != null) currentParkour.exit(user);

		user.inventoryUserInterfaces = null;

		user.board.clearScoreboard();
		user.board = null;

		user.localizer = null;
	}

}
