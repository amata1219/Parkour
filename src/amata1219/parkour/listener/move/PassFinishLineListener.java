package amata1219.parkour.listener.move;

import java.util.Set;

import org.bukkit.entity.Player;

import amata1219.amalib.message.MessageTemplate;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.message.TimeFormat;
import amata1219.parkour.parkour.RegionWithBorders;
import amata1219.parkour.parkour.Reward;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.parkour.RecordSet;
import amata1219.parkour.user.User;

public class PassFinishLineListener extends PassRegionBoundaryAbstractListener {

	public PassFinishLineListener() {
		super(ParkourSet.getInstance().chunksToFinishLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, RegionWithBorders from, RegionWithBorders to) {
		//フィニッシュラインに初めて踏み込んだのでなければ戻る
		if(user.parkourPlayingNow == null || from != null || to == null)
			return;

		//アスレ名を取得する
		String parkourName = parkour.name;

		//クリア済みのアスレ名リストを取得する
		Set<String> clearedParkourNames = user.clearedParkourNames;

		//クリアした事があるかどうか
		boolean haveCleared = clearedParkourNames.contains(parkourName);

		//クリア済みのアスレとして記録する(コレクションにはSetを用いているため要素の重複は起こらない)
		user.clearedParkourNames.add(parkourName);

		//アスレをプレイし始めた時間を取得する
		long timeToStartPlaying = user.timeToStartPlaying;

		String playerName = player.getName();

		if(timeToStartPlaying > 0){
			//ゴールタイムを秒単位で出す
			float time = (System.currentTimeMillis() - user.timeToStartPlaying) / 1000F;

			RecordSet records = parkour.records;

			//ゴールタイムを記録する
			records.record(user.uuid, time);

			//記録をソートする
			records.sort();

			//表示例: amata1219 cleared Update11 @ 00:01:23.231!
			MessageTemplate.applyWithColor("$0 cleared $1 @ $2!", playerName, parkourName, TimeFormat.format(time)).broadcast();
		}else{
			//表示例: amata1219 cleared Update6!
			MessageTemplate.applyWithColor("$0 cleared $1!", playerName, parkourName).broadcast();
		}

		//タイムを削除する
		user.timeToStartPlaying = 0;

		//遊んでいるアスレを削除する
		user.parkourPlayingNow = null;

		//Updateの場合
		if(parkour.isUpdate()){
			//アスレのランクを取得する
			int rank = Integer.parseInt(parkour.getColorlessName().replace("Update", ""));

			//プレイヤーのランクがこれより低い場合
			if(user.updateRank < rank){
				//ランクを更新する
				user.updateRank = rank;

				ApplyRankToDisplayName.apply(user);

				//表示例: Rank up @ amata1219's update rank is 8!
				MessageTemplate.applyWithColor("&b-Rank up &7-@ &b-$0's update rank is $1-&r-&b-!", playerName, rank).broadcast();
			}

		//Extendの場合
		}else if(parkour.isExtend()){
			//アスレのランクを取得する
			int rank = Integer.parseInt(parkour.getColorlessName().replace("Extend", ""));

			//プレイヤーのランクがこれより低い場合
			if(user.extendRank < rank){
				//ランクを更新する
				user.extendRank = rank;

				//表示例: Rank up @ amata1219's extend rank is 7!
				MessageTemplate.applyWithColor("&b-Rank up &7-@ &b-$0's extend rank is $1-&r-&b-!", playerName, rank).broadcast();
			}
		}

		//報酬が無いアスレであれば戻る
		if(!parkour.hasReward()) return;

		Reward reward = Reward.valueOf(parkour.getColorlessName());

		int coins = haveCleared ? reward.afterSecondTime : reward.first;

		//報酬のコインを与える
		user.depositCoins(coins);

		//表示例: Gave amata1219 1000 coins as a reward!
		MessageTemplate.applyWithColor("&b-Gave $0 $1 coins as a reard!", playerName, coins).display(player);
	}

}
