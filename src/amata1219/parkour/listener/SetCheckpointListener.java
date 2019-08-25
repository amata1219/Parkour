package amata1219.parkour.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.parkour.chunk.ChunksToObjectsMap;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.string.message.MessageTemplate;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class SetCheckpointListener implements Listener {

	private final UserSet users = UserSet.getInstnace();
	private final ChunksToObjectsMap<ParkourRegion> chunksToCheckAreasMap = ParkourSet.getInstance().chunksToCheckAreasMap;

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){
		//固定キャンセル
		event.setCancelled(true);

		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		//足を地に着いていなければ戻る
		if(!player.isOnGround()) return;

		//今いるアスレが無ければ戻る
		if(!user.isOnCurrentParkour()) return;

		Location location = player.getLocation();

		//プレイヤーの現在地に存在するチェックエリアのリストを取得する
		List<ParkourRegion> checkAreas = chunksToCheckAreasMap.get(location);

		//チェックエリアが存在しなければ戻る
		if(checkAreas.isEmpty()) return;

		ParkourRegion checkArea = null;

		for(ParkourRegion area : checkAreas){
			if(!area.isIn(location)) continue;

			checkArea = area;
			break;
		}

		//チェックエリア内にいなければ戻る
		if(checkArea == null) return;

		//チェックエリアがあるアスレを取得する
		Parkour parkour = checkArea.parkour;

		//プレイヤーが今いるアスレでなければ戻る
		if(!user.currentParkour.equals(parkour)) return;

		//メジャーチェックエリア番号を取得する
		int majorCheckAreaNumber = parkour.checkAreas.getMajorCheckAreaNumber(checkArea);

		//不正な番号であれば戻る
		if(majorCheckAreaNumber < 0) return;

		//チェックポイントとして設定する
		user.checkpoints.setCheckpoint(parkour, majorCheckAreaNumber, new ImmutableLocation(location));

		MessageTemplate.capply("&b-Set checkpoint @ $0", majorCheckAreaNumber + 1).displayOnActionBar(player);
	}

}
