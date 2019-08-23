package amata1219.parkour.listener;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.function.PlayerLocaleChange;
import amata1219.parkour.parkour.Parkour;
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

		//オンラインプレイヤーの数を更新する
		users.getOnlineUsers().stream()
		.map(User::getBoard)
		.filter(Objects::nonNull)
		.forEach(InformationBoard::updateOnlinePlayers);

		//もし5秒以内に言語設定に変更があればスコアボードの表示を更新する
		PlayerLocaleChange.applyIfLocaleChanged(user, 100, u -> u.board.updateAll());

		//プレイヤー名にランクを表示させる
		ApplyRankToDisplayName.apply(user);

		//最終ログアウト時にいたアスレを取得する
		Parkour lastParkour = user.parkourWithNow;

		//最終ログアウト時にどこかのアスレにいる場合
		if(lastParkour != null){
			//再参加させる
			lastParkour.entry(user);

			user.localizer.mapplyAll("$0-&r-&b-への挑戦を再開しました！ | $0 &r-&b-Challenge Restarted!", lastParkour.name).displayOnActionBar(player);

			//タイムアタックの途中であれば経過時間からスタート時のタイムを設定する
			if(user.isPlayingInParkour() && user.timeElapsed > 0){
				user.timeToStartPlaying = System.currentTimeMillis() - user.timeElapsed;
				user.timeElapsed = 0;
			}
		}
	}

}
