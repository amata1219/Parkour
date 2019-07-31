package amata1219.parkour.listener;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class DisplayRegionBorderListener implements Listener {

	private final Map<UUID, User> users = Main.getUserSet().users;

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		User user = users.get(player.getUniqueId());

		//最後にいたアスレを取得する
		Parkour lastParkour = user.currentParkour;

		//無ければ戻る
		if(lastParkour == null)
			return;

		lastParkour.join(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		User user = users.get(player);

		//今いるアスレを取得する
		Parkour currentParkour = user.currentParkour;

		//無ければ戻る
		if(currentParkour == null)
			return;

		currentParkour.quit(player);
	}

}
