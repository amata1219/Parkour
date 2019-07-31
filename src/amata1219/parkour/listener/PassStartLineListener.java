package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.amalib.text.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.message.Messenger;
import amata1219.parkour.parkour.RegionBorderDisplayer;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class PassStartLineListener extends PassRegionBoundaryAbstractListener {

	public PassStartLineListener() {
		super(Main.getParkourSet().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, RegionBorderDisplayer from, RegionBorderDisplayer to) {
		//スタートラインに初めて踏み込んだ時
		if(from == null && to != null){

			//プレイ中のアスレとして設定する
			user.currentlyPlayingParkour = parkour;

			//プレイし始めた時間を記録する
			user.timeToStartPlaying = System.currentTimeMillis();

			//表示例: Challenge Started @ Update10
			Messenger.sendActionBarMessage(player, StringTemplate.format("Challenge Started @ $0!", parkour.name));

		//スタートラインからスポーン地点側に踏み込んだ時
		}else if(from != null && to == null){

			//プレイ中のアスレを削除する
			user.currentlyPlayingParkour = null;

			//タイムを削除する
			user.timeToStartPlaying = 0L;

			Messenger.sendActionBarMessage(player, StringTemplate.format("Reset Your Timer!", parkour.name));
		}
	}

}
