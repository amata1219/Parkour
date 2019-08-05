package amata1219.parkour.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.parkour.RegionWithBorders;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public abstract class PassRegionBoundaryAbstractListener implements Listener {

	private final UserSet users = UserSet.getInstnace();
	private final ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap;

	protected PassRegionBoundaryAbstractListener(ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap){
		this.chunksToRegionsMap = chunksToRegionsMap;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Location from = event.getFrom();

		//元々いた地点に存在していた領域
		RegionWithBorders fromRegion = null;

		for(RegionWithBorders region : chunksToRegionsMap.get(from)){
			if(!region.region.isIn(from))
				continue;

			fromRegion = region;
			break;
		}

		Location to = event.getTo();

		RegionWithBorders toRegion = null;

		for(RegionWithBorders region : chunksToRegionsMap.get(to)){
			if(!region.region.isIn(to))
				continue;

			toRegion = region;
			break;
		}

		Player player = event.getPlayer();
		User user = users.getUser(player);

		//アスレを取得する
		Parkour parkour = (fromRegion != null ? fromRegion.parkour : (toRegion != null ? toRegion.parkour : null));

		//アスレが存在しなければ戻る
		if(parkour == null)
			return;

		onMove(player, user, parkour, fromRegion, toRegion);
	}

	public abstract void onMove(Player player, User user, Parkour parkour, RegionWithBorders from, RegionWithBorders to);

}
