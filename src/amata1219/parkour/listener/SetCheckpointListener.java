package amata1219.parkour.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class SetCheckpointListener implements Listener {

	private final Users users = Users.getInstnace();
	private final ChunksToObjectsMap<ParkourRegion> chunksToCheckAreasMap = Parkours.getInstance().chunksToCheckAreasMap;

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){
		//固定キャンセル
		event.setCancelled(true);

		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		//足を地に着いていなければ戻る
		if(!player.isOnGround()) return;

		//アスレにいなければ戻る
		if(user.currentParkour == null) return;

		Location location = player.getLocation();

		//プレイヤーの現在地に存在するチェックエリアのリストを取得する
		List<ParkourRegion> checkAreas = chunksToCheckAreasMap.get(location);

		//チェックエリアが存在しなければ戻る
		if(checkAreas.isEmpty()) return;

		//リストの最初の要素をチェックエリアとして取得する
		ParkourRegion checkArea = checkAreas.get(0);

		//領域内にいなければ戻る
		if(!checkArea.isIn(location)) return;

		//チェックエリアがあるアスレを取得する
		Parkour parkour = checkArea.parkour;

		//プレイヤーが今いるアスレでなければ戻る
		if(!user.currentParkour.equals(parkour)) return;

		//チェックエリアの番号を取得する
		int checkAreaNumber = parkour.checkAreas.getCheckAreaNumber(checkArea);

		//不正な番号であれば戻る
		if(checkAreaNumber <= -1) return;

		//チェックポイントとして設定する
		user.checkpoints.setCheckpoint(parkour, checkAreaNumber, new ImmutableEntityLocation(location));

		MessageTemplate.capply("&b-Set checkpoint @ $0", checkAreaNumber).displayOnActionBar(player);
	}

}
