package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.GraphicalRegion;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class EnterStartLineListener extends PassRegionBoundaryAbstractListener {

	public EnterStartLineListener() {
		super(Main.getParkourSet().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, GraphicalRegion from, GraphicalRegion to) {
		if(from == null && to != null){
			//スタートラインに初めて踏み込んだ時

			user.currentlyPlayingParkour = parkour;
			user.timeToStartPlaying = System.currentTimeMillis();

			user.sendMessageToDisappearAutomatically("§c: DEBUG @ ENTER START LINE FROM SPAWN AREA");
		}else if(from != null && to == null){
			//スタートラインからスポーン地点側に戻った時

			user.currentlyPlayingParkour = null;
			user.timeToStartPlaying = 0L;

			user.sendMessageToDisappearAutomatically("§c: DEBUG @ ENTER SPAWN AREA FROM START LINE");
		}
	}

}
