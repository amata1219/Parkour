package amata1219.parkour.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.message.MessageTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.RegionBorderDisplayer;
import amata1219.parkour.user.User;

public class SetCheckpointListener implements Listener {

	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToCheckAreasMap = Main.getParkourSet().chunksToCheckAreasMap;

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){
		event.setCancelled(true);

		Player player = event.getPlayer();
		User user = Main.getUserSet().users.get(player.getUniqueId());

		//足を地に着いていなければ戻る
		if(!player.isOnGround())
			return;

		//現在アスレをプレイ中でなければ戻る
		if(!user.isPlayingParkour())
			return;

		Location location = player.getLocation();

		//プレイヤーの現在地に存在するチェックエリアのリストを取得する
		List<RegionBorderDisplayer> areas = chunksToCheckAreasMap.get(location);

		//チェックエリアが存在しなければ戻る
		if(areas.isEmpty())
			return;

		//リストの最初の要素をチェックエリアとして取得する
		RegionBorderDisplayer area = areas.get(0);

		//領域内にいなければ戻る
		if(!area.region.isIn(location))
			return;

		Parkour parkour = area.parkour;

		if(!user.currentlyPlayingParkour.equals(parkour))
			return;

		int areaNumber = parkour.getCheckAreaNumber(area);

		//チェックポイントとして設定する
		user.setCheckPoint(parkour, areaNumber, location);

		MessageTemplate.applyWithColor("&b-Set checkpoint @ $0", areaNumber).displayOnActionBar(player);
	}

}
