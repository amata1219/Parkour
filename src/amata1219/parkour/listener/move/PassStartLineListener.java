package amata1219.parkour.listener.move;

import org.bukkit.entity.Player;

import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.parkour.parkour.RegionWithBorders;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.user.User;

public class PassStartLineListener extends PassRegionBoundaryAbstractListener {

	public PassStartLineListener() {
		super(ParkourSet.getInstance().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, RegionWithBorders from, RegionWithBorders to) {
		String parkourName = parkour.name;

		//スタートラインに初めて踏み込んだ時
		if(from == null && to != null){

			//プレイ中のアスレとして設定する
			user.parkourPlayingNow = parkour;

			//プレイし始めた時間を記録する
			user.timeToStartPlaying = System.currentTimeMillis();

			//表示例: Challenge started @ Update10
			MessageTemplate.applyWithColor("&b-Challenge started @ $0-&r-&b-!", parkourName).displayOnActionBar(player);

		//スタートラインからスポーン地点側に踏み込んだ時
		}else if(from != null && to == null){

			//プレイ中のアスレを削除する
			user.parkourPlayingNow = null;

			//タイムを削除する
			user.timeToStartPlaying = 0L;

			MessageColor.color("&b-Reset your timer!").displayOnActionBar(player);
		}
	}

}
