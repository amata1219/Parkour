package amata1219.parkour.listener.move;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.RegionBorderDisplayer;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public abstract class PassRegionBoundaryAbstractListener implements Listener {

	private final ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap;

	protected PassRegionBoundaryAbstractListener(ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap){
		this.chunksToRegionsMap = chunksToRegionsMap;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Location from = event.getFrom();

		//元々いた地点に存在していた領域
		RegionBorderDisplayer fromRegion = null;

		for(RegionBorderDisplayer region : chunksToRegionsMap.get(from)){
			if(!region.region.isIn(from))
				continue;

			fromRegion = region;
			break;
		}

		Location to = event.getTo();

		RegionBorderDisplayer toRegion = null;

		for(RegionBorderDisplayer region : chunksToRegionsMap.get(to)){
			if(!region.region.isIn(to))
				continue;

			toRegion = region;
			break;
		}

		Player player = event.getPlayer();
		User user = Main.getUserSet().users.get(player.getUniqueId());
		Parkour parkour = (fromRegion != null ? fromRegion.parkour : (toRegion != null ? toRegion.parkour : null));
		if(parkour == null)
			return;

		onMove(player, user, parkour, fromRegion, toRegion);
	}

	public abstract void onMove(Player player, User user, Parkour parkour, RegionBorderDisplayer from, RegionBorderDisplayer to);

}
