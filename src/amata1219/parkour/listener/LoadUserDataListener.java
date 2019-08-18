package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.user.InventoryUIs;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class LoadUserDataListener implements PlayerJoinListener {

	@Override
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		User user = Users.getInstnace().getUser(player);
		user.localizer = new Localizer(player);
		user.inventoryUIs = new InventoryUIs(user);
		ApplyRankToDisplayName.apply(user);

		//タイムアタックの途中であれば
		if(user.isPlayingWithParkour() && user.elapsedTime > 0){
			user.timeToStartPlaying = System.currentTimeMillis() - user.elapsedTime;
			user.elapsedTime = 0;
		}
	}

}
