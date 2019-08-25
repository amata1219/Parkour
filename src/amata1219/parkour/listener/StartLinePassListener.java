package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.User;

public class StartLinePassListener extends RegionBoundaryPassAbstractListener {

	public StartLinePassListener() {
		super(ParkourSet.getInstance().chunksToStartLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		//スタートラインの領域から何もない領域に進もうとしたのでなければ戻る
		if(from != null || to == null) return;

		boolean timeAttackEnable = parkour.timeAttackEnable;
		Localizer localizer = user.localizer;

		//スポーン地点側に戻ってきた場合
		if(user.isPlayingParkour()){
			user.parkourPlayingNow = null;

			//タイムアタックが有効でなければ戻る
			if(!timeAttackEnable) return;

			user.startTime = 0;

			localizer.mcolor("&c-タイマーをリセットしました | &c-Reset your timer").displayOnActionBar(player);

		//アスレをプレイし始めた場合
		}else{
			user.parkourPlayingNow = parkour;

			//タイムアタックが有効であればプレイし始めた時間を記録する
			if(timeAttackEnable) user.startTime = System.currentTimeMillis();

			localizer.mapplyAll("$0-&r-&b-への挑戦を始めました！ | $0 &r-&b-Challenge Started!", parkour.name).displayOnActionBar(player);
		}
	}

}
