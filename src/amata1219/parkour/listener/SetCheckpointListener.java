package amata1219.parkour.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.parkour.RegionWithBorders;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public class SetCheckpointListener implements Listener {

	private final UserSet users = UserSet.getInstnace();
	private final ChunksToObjectsMap<RegionWithBorders> chunksToCheckAreasMap = ParkourSet.getInstance().chunksToCheckAreasMap;

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){
		event.setCancelled(true);

		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		//足を地に着いていなければ戻る
		if(!player.isOnGround()) return;

		//現在アスレをプレイ中でなければ戻る
		if(!user.isPlayignWithParkour()) return;

		Location location = player.getLocation();

		//プレイヤーの現在地に存在するチェックエリアのリストを取得する
		List<RegionWithBorders> checkAreas = chunksToCheckAreasMap.get(location);

		//チェックエリアが存在しなければ戻る
		if(checkAreas.isEmpty()) return;

		//リストの最初の要素をチェックエリアとして取得する
		RegionWithBorders checkArea = checkAreas.get(0);

		//領域内にいなければ戻る
		if(!checkArea.region.isIn(location)) return;

		//チェックエリアがあるアスレを取得する
		Parkour parkour = checkArea.parkour;

		//プレイヤーが今遊んでいるアスレでなければ戻る
		if(!user.parkourPlayingNow.equals(parkour)) return;

		//チェックエリアの番号を取得する
		int areaNumber = parkour.checkAreas.getCheckAreaNumber(checkArea);

		//不正な番号であれば戻る
		if(areaNumber <= -1) return;

		//チェックポイントとして設定する
		user.setCheckpoint(parkour, areaNumber, new ImmutableEntityLocation(location));

		MessageTemplate.applyWithColor("&b-Set checkpoint @ $0", areaNumber).displayOnActionBar(player);
	}

}
