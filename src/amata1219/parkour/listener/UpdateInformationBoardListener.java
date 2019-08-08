package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.event.PlayerJumpEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.user.InformationBoard;
import amata1219.parkour.user.UserSet;

public class UpdateInformationBoardListener implements PlayerJoinListener, PlayerQuitListener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		users.getUser(event.getPlayer()).board.loadScoreboard();
		users.getOnlineUsers().stream().map(user -> user.board).forEach(InformationBoard::updateOnlinePlayers);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		users.getUser(event.getPlayer()).board.clearScoreboard();
	}

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		users.getUser(event.getPlayer()).board.updateJumps();;
	}

}
