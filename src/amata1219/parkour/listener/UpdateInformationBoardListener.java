package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.amalib.event.PlayerJumpEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.parkour.user.InformationBoard;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class UpdateInformationBoardListener implements PlayerJoinListener, PlayerQuitListener {

	private final Users users = Users.getInstnace();

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		User user = getUser(event.getPlayer());

		//スコアボードの管理インスタンスを作成する
		user.board = new InformationBoard(user);
		user.board.loadScoreboard();

		users.getOnlineUsers().stream().map(ur -> ur.getInformationBoard()).forEach(InformationBoard::updateOnlinePlayers);
	}

	@Override
	public void onQuit(PlayerQuitEvent event) {
		User user = getUser(event.getPlayer());

		user.board.clearScoreboard();
		user.board = null;
	}

	@EventHandler
	public void incrementJumps(PlayerJumpEvent event){
		getBoard(event.getPlayer()).updateJumps();
	}

	private InformationBoard getBoard(Player player){
		return getUser(player).getInformationBoard();
	}

	private User getUser(Player player){
		return users.getUser(player);
	}

}
