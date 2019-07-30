package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.amalib.text.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.message.Messenger;
import amata1219.parkour.message.TimeFormat;
import amata1219.parkour.parkour.RegionBorder;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class PassFinishLineListener extends PassRegionBoundaryAbstractListener {

	public PassFinishLineListener() {
		super(Main.getParkourSet().chunksToFinishLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, RegionBorder from, RegionBorder to) {
		if(from == null && to != null){
			//初めてフィニッシュラインに踏み込んだ時

			String parkourName = parkour.name;

			//クリア済みのアスレとして記録する(コレクションにはSetを用いているため要素の重複は起こらない)
			user.clearedParkourNames.add(parkourName);

			//ゴールタイムを秒単位で出す
			float time = (System.currentTimeMillis() - user.timeToStartPlaying) / 1000F;

			//遊んでいるアスレを削除する
			user.currentlyPlayingParkour = null;

			//タイムを削除する
			user.timeToStartPlaying = 0L;

			//表示例: amata1219 > Cleared in 00:01:23.231 @ Update11！
			Messenger.broadcastMessage(StringTemplate.format("$0 > Cleared in $1 @ $2", player.getName(), TimeFormat.format(time), parkourName));
		}
	}

}
