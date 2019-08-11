package amata1219.parkour.listener;

import java.util.Set;

import org.bukkit.entity.Player;

import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.Records;
import amata1219.parkour.user.User;
import amata1219.parkour.util.TimeFormat;

public class PassFinishLineListener extends PassRegionBoundaryAbstractListener {

	public PassFinishLineListener() {
		super(Parkours.getInstance().chunksToFinishLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		//アスレをゴールしたのでなければ戻る
		if(user.parkourPlayingNow == null || from != null || to == null) return;

		//アスレ名を取得する
		String parkourName = parkour.name;

		//タイムアタックが有効かどうか
		boolean enableTimeAttack = parkour.enableTimeAttack;

		//クリア済みのアスレ名リストを取得する
		Set<String> clearedParkourNames = user.clearedParkourNames;

		//クリアした事があるかどうか
		boolean haveCleared = clearedParkourNames.contains(parkourName);

		//クリア済みのアスレとして記録する(コレクションにはSetを用いているため要素の重複は起こらない)
		user.clearedParkourNames.add(parkourName);

		String playerName = player.getName();

		//タイムアタックが有効の場合
		if(enableTimeAttack){
			//ゴールタイムを秒単位で出す
			float time = (System.currentTimeMillis() - user.timeToStartPlaying) / 1000F;

			//タイムを削除する
			user.timeToStartPlaying = 0;

			Records records = parkour.records;

			//ゴールタイムを記録する
			records.record(user.uuid, time);

			//記録をソートする
			records.sort();

			//表示例: amata1219 cleared Update11 @ 00:01:23.231!
			MessageTemplate.capply("$0 cleared $1 @ $2!", playerName, parkourName, TimeFormat.format(time)).broadcast();
		}else{
			//表示例: amata1219 cleared Update6!
			MessageTemplate.capply("$0 cleared $1!", playerName, parkourName).broadcast();
		}

		//遊んでいるアスレを削除する
		user.parkourPlayingNow = null;

		//Updateの場合
		if(parkour.isUpdate()){
			//アスレのランクを取得する
			int rank = Integer.parseInt(parkour.getColorlessName().replace("Update", ""));

			//プレイヤーのランクがこれより低い場合
			if(user.getUpdateRank() < rank){
				//ランクを更新する
				user.incrementUpdateRank();

				ApplyRankToDisplayName.apply(user);

				//表示例: Rank up @ amata1219's update rank is 8!
				MessageTemplate.capply("&b-Rank up &7-@ &b-$0's update rank is $1-&r-&b-!", playerName, rank).broadcast();
			}

		//Extendの場合
		}else if(parkour.isExtend()){
			//アスレのランクを取得する
			int rank = Integer.parseInt(parkour.getColorlessName().replace("Extend", ""));

			//プレイヤーのランクがこれより低い場合
			if(user.getExtendRank() < rank){
				//ランクを更新する
				user.incrementExtendRank();

				//表示例: Rank up @ amata1219's extend rank is 7!
				MessageTemplate.capply("&b-Rank up &7-@ &b-$0's extend rank is $1-&r-&b-!", playerName, rank).broadcast();
			}
		}

		int coins = parkour.rewards.getReward(haveCleared ? 1 : 0);

		//報酬のコインを与える
		user.depositCoins(coins);

		//表示例: Gave amata1219 1000 coins as a reward!
		MessageTemplate.capply("&b-Gave $0 $1 coins as a reward!", playerName, coins).display(player);
	}

}
