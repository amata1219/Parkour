package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.event.PlayerJumpEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.parkour.user.InformationBoard;
import amata1219.parkour.user.Users;

public class UpdateInformationBoardListener implements PlayerJoinListener {

	private final Users users = Users.getInstnace();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		users.getOnlineUsers().stream().map(user -> user.getInformationBoard()).forEach(InformationBoard::updateOnlinePlayers);
	}

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		getBoard(event.getPlayer()).updateJumps();
	}

	private InformationBoard getBoard(Player player){
		return users.getUser(player).getInformationBoard();
	}

}
