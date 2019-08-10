package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.parkour.parkour.ParkourRegion;
import amata1219.amalib.string.message.MessageColor;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class PassStartLineListener extends PassRegionBoundaryAbstractListener {

	public PassStartLineListener() {
		super(Parkours.getInstance().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		String parkourName = parkour.name;

		//タイムアタックが有効かどうか
		boolean enableTimeAttack = parkour.enableTimeAttack;

		//スタートラインに初めて踏み込んだ時
		if(from == null && to != null){

			//プレイ中のアスレとして設定する
			user.parkourPlayingNow = parkour;

			//タイムアタックが有効の場合
			if(enableTimeAttack){
				//プレイし始めた時間を記録する
				user.timeToStartPlaying = System.currentTimeMillis();

				//表示例: Challenge started @ Update10
				MessageTemplate.capply("&b-Challenge started @ $0-&r-&b-!", parkourName).displayOnActionBar(player);
			}

		//スタートラインからスポーン地点側に踏み込んだ時
		}else if(from != null && to == null){

			//プレイ中のアスレを削除する
			user.parkourPlayingNow = null;

			//タイムアタックが有効の場合
			if(enableTimeAttack){
				//タイムを削除する
				user.timeToStartPlaying = 0L;

				MessageColor.color("&b-Reset your timer!").displayOnActionBar(player);
			}
		}
	}

}
