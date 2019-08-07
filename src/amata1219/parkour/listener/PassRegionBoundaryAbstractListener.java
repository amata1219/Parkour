package amata1219.parkour.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.parkour.OldParkourRegion;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public abstract class PassRegionBoundaryAbstractListener implements Listener {

	private final UserSet users = UserSet.getInstnace();
	private final ChunksToObjectsMap<OldParkourRegion> chunksToRegionsMap;

	protected PassRegionBoundaryAbstractListener(ChunksToObjectsMap<OldParkourRegion> chunksToRegionsMap){
		this.chunksToRegionsMap = chunksToRegionsMap;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Location from = event.getFrom();

		//元々いた地点に存在していた領域
		OldParkourRegion fromRegion = null;

		for(OldParkourRegion region : chunksToRegionsMap.get(from)){
			if(!region.region.isIn(from))
				continue;

			fromRegion = region;
			break;
		}

		Location to = event.getTo();

		OldParkourRegion toRegion = null;

		for(OldParkourRegion region : chunksToRegionsMap.get(to)){
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

	public abstract void onMove(Player player, User user, Parkour parkour, OldParkourRegion from, OldParkourRegion to);

}
