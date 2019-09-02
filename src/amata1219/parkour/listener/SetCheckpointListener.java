package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.text.BilingualText;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class SetCheckpointListener implements Listener {

	private final UserSet users = UserSet.getInstnace();

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){
		//固定キャンセル
		event.setCancelled(true);

		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		if(!user.isPlayingParkour() || !user.isOnCheckArea()) return;

		ParkourRegion checkArea = user.currentCheckArea;

		//チェックエリアがあるアスレを取得する
		Parkour parkour = checkArea.parkour;

		//プレイヤーが今いるアスレでなければ戻る
		if(!user.currentParkour.equals(parkour)) return;

		//メジャーチェックエリア番号を取得する
		int majorCheckAreaNumber = parkour.checkAreas.getMajorCheckAreaNumber(checkArea);

		//不正な番号であれば戻る
		if(majorCheckAreaNumber < 0) return;

		//地に足をついていなければ戻る
		if(player.isOnGround()){

			return;
		}

		//チェックポイントとして設定する
		user.checkpoints.setCheckpoint(parkour, majorCheckAreaNumber, new ImmutableLocation(player.getLocation()));

		BilingualText.stream("$colorチェックポイント$numberを設定しました", "$colorSet checkpoint$number")
		.setAttribute("$color", parkour.prefixColor)
		.setAttribute("$number", majorCheckAreaNumber + 1)
		.color()
		.setReceiver(player)
		.sendActionBarMessage();
	}

}
