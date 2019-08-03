package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.RegionBorderDisplayer;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class PassStartLineListener extends PassRegionBoundaryAbstractListener {

	public PassStartLineListener() {
		super(Main.getParkourSet().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, RegionBorderDisplayer from, RegionBorderDisplayer to) {
		String parkourName = parkour.name;

		//スタートラインに初めて踏み込んだ時
		if(from == null && to != null){

			//プレイ中のアスレとして設定する
			user.currentlyPlayingParkour = parkour;

			//プレイし始めた時間を記録する
			user.timeToStartPlaying = System.currentTimeMillis();

			//表示例: Challenge started @ Update10
			MessageTemplate.applyWithColor("&b-Challenge started @ $0-&r-&b-!", parkourName).displayOnActionBar(player);

		//スタートラインからスポーン地点側に踏み込んだ時
		}else if(from != null && to == null){

			//プレイ中のアスレを削除する
			user.currentlyPlayingParkour = null;

			//タイムを削除する
			user.timeToStartPlaying = 0L;

			MessageColor.color("&b-Reset your timer!").displayOnActionBar(player);
		}
	}

}
