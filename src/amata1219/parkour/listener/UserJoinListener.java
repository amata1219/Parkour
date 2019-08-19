package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.user.InformationBoard;
import amata1219.parkour.user.InventoryUserInterfaces;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class UserJoinListener implements PlayerJoinListener {

	private final Users users = Users.getInstnace();

	@Override
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		User user = users.getUser(player);

		user.localizer = new Localizer(player);
		user.inventoryUserInterfaces = new InventoryUserInterfaces(user);

		user.board = new InformationBoard(user);
		user.board.loadScoreboard();

		users.getOnlineUsers().stream()
		.filter(ur -> ur.board != null)
		.map(ur -> ur.board)
		.forEach(InformationBoard::updateOnlinePlayers);

		//プレイヤー名にランクを表示させる
		ApplyRankToDisplayName.apply(user);

		//タイムアタックの途中であれば
		if(user.isPlayingWithParkour() && user.elapsedTime > 0){
			user.timeToStartPlaying = System.currentTimeMillis() - user.elapsedTime;
			user.elapsedTime = 0;
		}
	}

}
