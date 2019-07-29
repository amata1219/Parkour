package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.parkour.parkour.GraphicalRegion;
import amata1219.parkour.user.User;

public class EnterStartLineListener extends EnterRegionAbstractListener {

	public EnterStartLineListener(ChunksToObjectsMap<GraphicalRegion> chunksToRegionsMap) {
		super(chunksToRegionsMap);
	}

	@Override
	public void onMove(Player player, User user, GraphicalRegion from, GraphicalRegion to) {
		/*
		 * spawn→start
		 * parkour→start
		 */

		if(from == null && to != null){
			//スタートラインに初めて踏み込んだ時

		}else if(from != null && to == null){
			//スタートラインからスポーン地点側に戻った時


		}
	}

}
