package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.RegionBorder;

public class SetCheckpointListener implements Listener {

	public final ChunksToObjectsMap<RegionBorder> chunksToCheckAreasMap = Main.getParkourSet().chunksToCheckAreasMap;

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event){


	}

}
