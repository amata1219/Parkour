package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.parkour.parkour.ParkourRegion;
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
		boolean enableTimeAttack = parkour.timeAttackEnable;

		//アスレをプレイし始めたのでなければ戻る
		if(from != null || to == null) return;

		//プレイ中のアスレとして設定する
		user.parkourPlayingNow = parkour;

		//タイムアタックが有効であればプレイし始めた時間を記録する
		if(enableTimeAttack) user.timeToStartPlaying = System.currentTimeMillis();

		user.localizer.mapplyAll("$0-&r-&b-への挑戦を始めました！ | $0 &r-&b-Challenge Started!", parkourName).displayOnActionBar(player);
	}

}
