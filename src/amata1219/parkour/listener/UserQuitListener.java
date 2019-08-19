package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class UserQuitListener implements PlayerQuitListener {

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		User user = Users.getInstnace().getUser(event.getPlayer());

		//タイムアタックの途中であれば経過時間を記録する
		if(user.isPlayingWithParkour() && user.parkourPlayingNow.timeAttackEnable) user.elapsedTime = System.currentTimeMillis() - user.timeToStartPlaying;

		user.inventoryUserInterfaces = null;
		user.localizer = null;

		user.board.clearScoreboard();
		user.board = null;
	}

}
