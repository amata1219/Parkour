package amata1219.parkour.listener;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.schedule.Sync;
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.function.PlayerLocaleChange;
import amata1219.parkour.user.StatusBoard;
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

		Localizer localizer = user.localizer = new Localizer(player);
		user.inventoryUserInterfaces = new InventoryUserInterfaces(user);

		StatusBoard statusBoard = user.statusBoard = new StatusBoard(user);
		statusBoard.loadScoreboard();

		//オンラインプレイヤーの数を更新する
		users.getOnlineUsers().stream()
		.map(User::statusBoard)
		.filter(Objects::nonNull)
		.forEach(StatusBoard::updateOnlinePlayers);

		//もし5秒以内に言語設定に変更があればスコアボードの表示を更新する
		PlayerLocaleChange.applyIfLocaleChanged(user, 100, u -> u.statusBoard.updateAll());

		//5秒後にPingの表示を更新する
		Sync.define(() -> statusBoard.updatePing()).executeLater(100);

		//プレイヤー名にランクを表示させる
		ApplyRankToDisplayName.apply(user);

		//最終ログアウト時にどこかのアスレにいた場合
		user.parkourWithNow().ifPresent(parkour -> {
			//再参加させる
			parkour.entry(user);

			localizer.mapplyAll("$0-&r-&b-への挑戦を再開しました！ | $0 &r-&b-Challenge Restarted!", parkour.name).displayOnActionBar(player);

			//タイムアタックの途中であれば経過時間からスタート時のタイムを再計算しセットする
			if(user.isPlayingParkour() && user.timeElapsed > 0){
				user.timeToStartPlaying = System.currentTimeMillis() - user.timeElapsed;
				user.timeElapsed = 0;
			}
		});
	}

}
