package amata1219.parkour.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.GraphicalRegion;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public abstract class EnterRegionAbstractListener implements Listener {

	private final ChunksToObjectsMap<GraphicalRegion> chunksToRegionsMap;

	protected EnterRegionAbstractListener(ChunksToObjectsMap<GraphicalRegion> chunksToRegionsMap){
		this.chunksToRegionsMap = chunksToRegionsMap;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Location from = event.getFrom();

		//元々いた地点に存在していた領域
		GraphicalRegion fromRegion = null;

		for(GraphicalRegion region : chunksToRegionsMap.get(from)){
			if(!region.region.isIn(from))
				continue;

			fromRegion = region;
			break;
		}

		Location to = event.getTo();

		GraphicalRegion toRegion = null;

		for(GraphicalRegion region : chunksToRegionsMap.get(to)){
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

	public abstract void onMove(Player player, User user, Parkour parkour, GraphicalRegion from, GraphicalRegion to);

}
